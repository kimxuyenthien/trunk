package de.gimik.app.allpresanapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.koushikdutta.ion.Ion;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.DateTimeUtility;
import de.gimik.app.allpresanapp.Utils.DecompressZip;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.*;
import de.gimik.app.allpresanapp.model.*;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import de.gimik.app.allpresanapp.viewmodel.ResultInfo;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Mike on 2/10/15.
 */

public class SplashActivity extends BaseActivity {

    // TAG
    public static final String TAG = SplashActivity.class.getSimpleName();

    private static final int FINISHED = 100;
    private static final int PIWIK_CALL = 10;


    // UI components
    @Bind(R.id.tv_status)
    TextView tvStatus;

    @Bind(R.id.progressBar1)
    ProgressBar progressBar;

    @Bind(R.id.iv_splash)
    ImageView iv_splash;

    private Thread startupThread;
    private EanDbAdapter eanDbAdapter;
    private EventDbAdapter eventDbAdapter;
    private IngredientDbAdapter ingredientDbAdapter;
    private ProductDbAdapter productDbAdapter;
    private ProductGroupDbAdapter productGroupDbAdapter;
    private ProductRecommendationDbAdapter productRecommendationDbAdapter;
    private ServiceQuestionDbAdapter serviceQuestionDbAdapter;
    private ProductRecommendationProductDbAdapter productRecommendationProductDbAdapter;
    private ProductWorldDbAdapter productWorldDbAdapter;
    private ProductIngredientDbAdapter productIngredientDbAdapter;

    boolean isOpenFromNotification = false;
    boolean isAcademyNews = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        eanDbAdapter = new EanDbAdapter(this);
        eventDbAdapter = new EventDbAdapter(this);
        ingredientDbAdapter = new IngredientDbAdapter(this);
        productDbAdapter = new ProductDbAdapter(this);
        productGroupDbAdapter = new ProductGroupDbAdapter(this);
        productRecommendationDbAdapter = new ProductRecommendationDbAdapter(this);
        productWorldDbAdapter = new ProductWorldDbAdapter(this);
        serviceQuestionDbAdapter = new ServiceQuestionDbAdapter(this);
        productRecommendationProductDbAdapter = new ProductRecommendationProductDbAdapter(this);
        productIngredientDbAdapter = new ProductIngredientDbAdapter(this);

        //random splash
        int[] images = {R.drawable.splash_1, R.drawable.splash_2, R.drawable.splash_3, R.drawable.splash_4, R.drawable.splash_5};
        Random random = new Random(System.currentTimeMillis());
        int posOfImage = random.nextInt(images.length - 1);
        iv_splash.setBackgroundResource(images[posOfImage]);

        Utils.saveIntForKey(
                this,
                Constant.SPLASH_IMAGE_POSITION, posOfImage);
        initProgressBar();

        try {
            isOpenFromNotification = getIntent().getBooleanExtra(Constant.ExtraKey.IS_OPEN_FROM_NOTIFICATION, false);
            isAcademyNews = getIntent().getBooleanExtra(Constant.ExtraKey.IS_ACADEMY_NEWS, false);
        } catch (Exception e) {
        }

        bootstrapApplication();

    }

    private void bootstrapApplication() {
        tvStatus.setText(R.string.checking_database);
        final String lastUpdated = Utils.getValueForKey(this, Constant.PREF_DB_TIMESTAMP);
        progressBar.setProgress(PIWIK_CALL);
        startupThread = new Thread() {
            @Override
            public void run() {
                doUpdateDB(lastUpdated);
            }
        };
        startupThread.start();
    }

    private void initProgressBar() {
        progressBar.setProgress(0);
        progressBar.setMax(FINISHED);
    }

    private void doUpdateDB(String dbTimestamp) {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            setupDB(dbTimestamp);
            //gotoMainActivity();
        } else {
            gotoMainActivity();
        }
    }

    private void setupDB(final String dbTimestamp) {
        tvStatus.setText(R.string.download);

        AllpresansAPI.getService().downloadDB(dbTimestamp == null ? DateTimeUtility.DBTimestampFormat.format(new Date()) : dbTimestamp)
                .flatMap(new Func1<Response<ResponseBody>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Response<ResponseBody> response) {
                        try {
                            Headers headers = response.headers();
                            String updateDate = headers.get(Constant.DATE_DOWNLOAD);
                            String newData = headers.get(Constant.NEWDATA);
                            ResultInfo info = new ResultInfo();
                            info.result = new TreeMap();
                            info.result.put(Constant.DATE_DOWNLOAD, updateDate);
                            info.result.put(Constant.NEWDATA, newData);
                            if (newData == null || "0".equals(newData) || response.body() == null) {
                                // nothing to update
                                return emptyData(info);
                            } else {
                                String downloadPath = getDownloadBasePath();
                                File downloadPathFile = new File(downloadPath);
                                if (!downloadPathFile.exists()) {
                                    downloadPathFile.mkdirs();
                                }
                                File zipFile = new File(downloadPathFile, "allpresan.zip");
                                if (zipFile.exists())
                                    zipFile.delete();

                                BufferedSink sink = Okio.buffer(Okio.sink(zipFile));
                                // you can access body of response
                                sink.writeAll(response.body().source());
                                sink.close();
                                info.result.put(Constant.ZIP_FILE, zipFile);
                                Observable<?> unzipObservable = unzipFile(info);
                                return unzipObservable;
                            }
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        gotoMainActivity();
                    }
                })
                .toBlocking()
                .single();

                /*
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        gotoMainActivity();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        gotoMainActivity();
                    }
                });
                */

    }

    /**
     * go to main screen
     */
    private void gotoMainActivity() {
        progressBar.setProgress(FINISHED);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constant.ExtraKey.IS_OPEN_FROM_NOTIFICATION, isOpenFromNotification);
        intent.putExtra(Constant.ExtraKey.IS_ACADEMY_NEWS, isAcademyNews);
        startActivity(intent);
        finish();
    }


    private void setUpDB(List<Observable<?>> imageDownloadObservables) {
        UpdateDBHelper updateDBHelper = UpdateDBHelper.getHelper(this);

        // update ean
        EanDbAdapter updateEanDbAdapter = new EanDbAdapter(this, updateDBHelper);
        List<Ean> eanList = updateEanDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(eanList)) {
            for (Ean obj : eanList) {
                eanDbAdapter.saveOrUpdate(obj);
            }
        }

        // update event
        EventDbAdapter updateEventDbAdapter = new EventDbAdapter(this, updateDBHelper);
        List<Event> eventList = updateEventDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(eventList)) {
            for (Event obj : eventList) {
                eventDbAdapter.saveOrUpdate(obj);
                if (!Utils.isNullOrEmpty(obj.getImage()))
                    imageDownloadObservables.add(downloadImage(obj.getImage(), getDownloadBasePath(), obj.getLocalImage()));
            }
        }

        // ingredient
        IngredientDbAdapter updateIngredientDbAdapter = new IngredientDbAdapter(this, updateDBHelper);
        List<Ingredient> ingredientList = updateIngredientDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(ingredientList)) {
            for (Ingredient obj : ingredientList) {
                ingredientDbAdapter.saveOrUpdate(obj);
            }
        }

        // product world
        ProductWorldDbAdapter updateProductWorldDbAdapter = new ProductWorldDbAdapter(this, updateDBHelper);
        List<ProductWorld> productWorldList = updateProductWorldDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productWorldList)) {
            for (ProductWorld obj : productWorldList) {
                if (!Utils.isNullOrEmpty(obj.getImage()))
                    imageDownloadObservables.add(downloadImage(obj.getImage(), getDownloadBasePath(), obj.getLocalImage()));
                if (!Utils.isNullOrEmpty(obj.getIcon())) {
                    imageDownloadObservables.add(downloadImage(obj.getIcon(), getDownloadBasePath(), obj.getLocalIcon()));
                }
                if (!Utils.isNullOrEmpty(obj.getProductGroupImage())) {
                    imageDownloadObservables.add(downloadImage(obj.getProductGroupImage(), getDownloadBasePath(), obj.getLocalGroupImage()));
                }
                if (!Utils.isNullOrEmpty(obj.getLogo())) {
                    imageDownloadObservables.add(downloadImage(obj.getLogo(), getDownloadBasePath(), obj.getLocalLogo()));
                }

                productWorldDbAdapter.saveOrUpdate(obj);
            }
        }

        // group
        ProductGroupDbAdapter updateProductGroupDbAdapter = new ProductGroupDbAdapter(this, updateDBHelper);
        List<ProductGroup> productGroupList = updateProductGroupDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productGroupList)) {
            for (ProductGroup obj : productGroupList) {
                if (!Utils.isNullOrEmpty(obj.getImage()))
                    imageDownloadObservables.add(downloadImage(obj.getImage(), getDownloadBasePath(), obj.getLocalImage()));

                productGroupDbAdapter.saveOrUpdate(obj);
            }
        }

        // product
        ProductDbAdapter updateProductDbAdapter = new ProductDbAdapter(this, updateDBHelper);
        List<Product> productList = updateProductDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productList)) {
            for (Product obj : productList) {
                if (!Utils.isNullOrEmpty(obj.getImage()))
                    imageDownloadObservables.add(downloadImage(obj.getImage(), getDownloadBasePath(), obj.getLocalImage()));
                if (obj.getIcon() != null) {
                    imageDownloadObservables.add(downloadImage(obj.getIcon(), getDownloadBasePath(), obj.getLocalIcon()));
                }

                productDbAdapter.saveOrUpdate(obj);
            }
        }

        // PRODUCT INGREDIENT
        ProductIngredientDbAdapter updateProductIngredientDbAdapter = new ProductIngredientDbAdapter(this, updateDBHelper);
        List<ProductIngredient> productIngredientList = updateProductIngredientDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productIngredientList)) {
            for (ProductIngredient obj : productIngredientList) {
                productIngredientDbAdapter.saveOrUpdate(obj);
            }
        }

        // PRODUCT RECOMMENDATION
        ProductRecommendationDbAdapter updateProductRecommendationDbAdapter = new ProductRecommendationDbAdapter(this, updateDBHelper);
        List<ProductRecommendation> productRecommendationList = updateProductRecommendationDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productRecommendationList)) {
            for (ProductRecommendation obj : productRecommendationList) {
                if (!Utils.isNullOrEmpty(obj.getImage()))
                    imageDownloadObservables.add(downloadImage(obj.getImage(), getDownloadBasePath(), obj.getLocalImage()));
                productRecommendationDbAdapter.saveOrUpdate(obj);
            }
        }

        // PRODUCT_RECOMMENDATION_PRODUCT
        ProductRecommendationProductDbAdapter updateProductRecommendationProductDbAdapter = new ProductRecommendationProductDbAdapter(this, updateDBHelper);
        List<ProductRecommendationProduct> productRecommendationProductList = updateProductRecommendationProductDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(productRecommendationProductList)) {
            for (ProductRecommendationProduct obj : productRecommendationProductList) {
                productRecommendationProductDbAdapter.saveOrUpdate(obj);
            }
        }

        // SERVICE_QUESTION
        ServiceQuestionDbAdapter updateServiceQuestionDbAdapter = new ServiceQuestionDbAdapter(this, updateDBHelper);
        List<ServiceQuestion> serviceQuestionList = updateServiceQuestionDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(serviceQuestionList)) {
            for (ServiceQuestion obj : serviceQuestionList) {
                serviceQuestionDbAdapter.saveOrUpdate(obj);
            }
        }

        // PRODUCT_REMOVED
        DataRemovedDbAdapter updateDataRemovedDbAdapter = new DataRemovedDbAdapter(this, updateDBHelper);
        List<DataRemoved> dataRemovedList = updateDataRemovedDbAdapter.queryAll();
        if (CollectionUtils.isNotEmpty(dataRemovedList)) {
            for (DataRemoved dataRemoved : dataRemovedList) {
                if (dataRemoved == null)
                    continue;
                if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(Ean.class))) {
                    eanDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(Event.class))) {
                    eventDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(Ingredient.class))) {
                    ingredientDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(Product.class))) {
                    productDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(ProductGroup.class))) {
                    productGroupDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(ProductWorld.class))) {
                    productWorldDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(ProductRecommendation.class))) {
                    productRecommendationDbAdapter.deleteById(dataRemoved.getData_id());
                } else if (dataRemoved.getTable_name().equalsIgnoreCase(DatabaseTableConfig.extractTableName(ServiceQuestion.class))) {
                    serviceQuestionDbAdapter.deleteById(dataRemoved.getData_id());
                }
            }
        }
    }

    private Observable<?> emptyData(ResultInfo resultInfo) {
        return Observable.just(resultInfo);
    }

    private Observable<?> unzipFile(final ResultInfo resultInfo) {
        return Observable.just(resultInfo).flatMap(new Func1<ResultInfo, Observable<?>>() {
            @Override
            public Observable<?> call(ResultInfo o) {
                List<Observable<?>> downloadImageObservables = new ArrayList();
                File file = (File) resultInfo.result.get(Constant.ZIP_FILE);
                try {
                    final File unzipFolder = new File(file.getParent(),
                            System.currentTimeMillis() + "");
                    if (!unzipFolder.exists()) {
                        unzipFolder.mkdirs();
                    }

                    if (!unzipFolder.exists()) {
                        unzipFolder.mkdirs();
                    }

                    // download finish
                    DecompressZip decomp = new DecompressZip(file.getPath(),
                            unzipFolder.getAbsolutePath() + File.separator);
                    decomp.unzip();

                    File[] files = unzipFolder.listFiles();
                    final File unzipFile = files[0];

                    File dbPath = getDatabasePath(UpdateDBHelper.getDataBaseName()).getParentFile();
                    if (!dbPath.exists()) {
                        dbPath.mkdirs();
                    }
                    final File dbFile = new File(dbPath + File.separator + UpdateDBHelper.getDataBaseName());
                    if (!dbFile.exists()) {
                        dbFile.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(dbFile);
                    FileInputStream fileInputStream = new FileInputStream(unzipFile);
                    IOUtils.copy(fileInputStream, fileOutputStream);
                    fileOutputStream.close();
                    fileInputStream.close();

                    setUpDB(downloadImageObservables);

                    if (CollectionUtils.isNotEmpty(downloadImageObservables)) {
                        // clean image cache
                        Ion.getDefault(getApplicationContext()).getCache().clear();
                        Ion.getDefault(getApplicationContext()).getBitmapCache().clear();

                        return Observable.zip(downloadImageObservables, new FuncN<Object>() {
                            @Override
                            public Object call(Object... args) {
                                return args;
                            }
                        }).doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                try {
                                    FileUtils.forceDelete(unzipFolder);
                                    FileUtils.forceDelete(dbFile);
                                    Utils.saveValueForKey(getApplicationContext(), Constant.PREF_DB_TIMESTAMP, resultInfo.result.get(Constant.DATE_DOWNLOAD) + "");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    return Observable.error(e);
                }
                return Observable.just(o);
            }
        });
    }

    private Observable<?> downloadImage(final String url, final String path, final String fileName) {
        Observable observable = AllpresansAPI.getService().fetchImage(url).doOnNext(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response<ResponseBody> response) {
                File file = new File(path, fileName);
                try {
                    BufferedSink sink = Okio.buffer(Okio.sink(file));
                    // you can access body of response
                    if (response.body() != null) {
                        sink.writeAll(response.body().source());
                        Log.d(TAG, "image url: " + url + " , with file name: " + fileName);
                    } else {
                        Log.d(TAG, "image NULL " + url + " with file name: " + fileName);
                    }
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return observable;
    }
}
