package com.example.clientsellingmedicine;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;


import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.clientsellingmedicine.Adapter.productAdapter;
import com.example.clientsellingmedicine.Adapter.feedAdapter;
import com.example.clientsellingmedicine.interfaces.IOnButtonAddToCartClickListener;
import com.example.clientsellingmedicine.interfaces.IOnFeedItemClickListener;
import com.example.clientsellingmedicine.interfaces.IOnItemClickListenerRecyclerView;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.Feed;
import com.example.clientsellingmedicine.models.Product;
import com.example.clientsellingmedicine.services.CartService;
import com.example.clientsellingmedicine.services.ProductService;
import com.example.clientsellingmedicine.services.ServiceBuilder;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements IOnItemClickListenerRecyclerView, IOnFeedItemClickListener, IOnButtonAddToCartClickListener {
    private Context mContext;

    IOnItemClickListenerRecyclerView listener;
    IOnFeedItemClickListener feedListener;
    productAdapter productAdapter;


    feedAdapter feedAdapter;
    TextView tv_DisplayAllTopSale,tv_DisplayAllTopDiscount,tvNumberCart,tv_DisplayAllNewProduct;
    RecyclerView rcvTopProductSelling, rcvTopProductsDiscount,rcvFeeds,rcvTopNewProduct;
    ImageView ivCart,ivNotification,iv_medicine,iv_health_care,iv_personal_care,iv_convenient_product,iv_functional_food,iv_mom_baby,iv_beauty_care,iv_medical_equipment;

    TextInputEditText searchText;

    FrameLayout redCircleCart;

    ImageSlider imageSlider;
    private SearchView searchView;
    private String lastQuery;

        private boolean isReloadData = false;

    public HomeFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);
        mContext = view.getContext();
        // toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false); // Tắt title mặc định của Toolbar
        }
        setHasOptionsMenu(true);

        addControl(view);
        addEvents();
        return view;


    }


    private void addControl(View view) {
        tv_DisplayAllTopSale = view.findViewById(R.id.tv_DisplayAllTopSale);
        rcvTopProductSelling = view.findViewById(R.id.rcvTopProductSelling);
        tv_DisplayAllTopDiscount = view.findViewById(R.id.tv_DisplayAllTopDiscount);
        tv_DisplayAllNewProduct = view.findViewById(R.id.tv_DisplayAllNewProduct);
        rcvTopProductsDiscount =view.findViewById(R.id.rcvTopProductDiscount);
        rcvFeeds = view.findViewById(R.id.rcvFeeds);
        rcvTopNewProduct = view.findViewById(R.id.rcvTopNewProduct);
        searchText = view.findViewById(R.id.search_text);
        ivCart = view.findViewById(R.id.ivCart);
        ivNotification = view.findViewById(R.id.ivNotification);
        tvNumberCart = view.findViewById(R.id.tvNumberCart);
        redCircleCart = view.findViewById(R.id.redCircleCart);
        imageSlider = view.findViewById(R.id.image_slider);
        iv_medicine = view.findViewById(R.id.iv_medicine);
        iv_health_care = view.findViewById(R.id.iv_health_care);
        iv_personal_care = view.findViewById(R.id.iv_personal_care);
        iv_convenient_product = view.findViewById(R.id.iv_convenient_product);
        iv_functional_food = view.findViewById(R.id.iv_functional_food);
        iv_mom_baby = view.findViewById(R.id.iv_mom_baby);
        iv_beauty_care = view.findViewById(R.id.iv_beauty_care);
        iv_medical_equipment = view.findViewById(R.id.iv_medical_equipment);
    }
    private void addEvents(){
        rcvFeeds.addItemDecoration(new DividerItemDecoration(HomeFragment.this.getContext(), DividerItemDecoration.VERTICAL));
        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.iv_medicine) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "1");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_health_care) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "2");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_personal_care) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "3");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_convenient_product) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "4");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_functional_food) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "5");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_mom_baby) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "6");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_beauty_care) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "7");
                startActivity(intent);
            } else if (v.getId() == R.id.iv_medical_equipment) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("categoryID", "8");
                startActivity(intent);
            }
        };
        iv_medicine.setOnClickListener(onClickListener);
        iv_health_care.setOnClickListener(onClickListener);
        iv_personal_care.setOnClickListener(onClickListener);
        iv_convenient_product.setOnClickListener(onClickListener);
        iv_functional_food.setOnClickListener(onClickListener);
        iv_mom_baby.setOnClickListener(onClickListener);
        iv_beauty_care.setOnClickListener(onClickListener);
        iv_medical_equipment.setOnClickListener(onClickListener);

        tv_DisplayAllTopSale.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductActivity.class);
            intent.putExtra("filtered", "top_sale");
            startActivity(intent);
        });

        tv_DisplayAllTopDiscount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductActivity.class);
            intent.putExtra("filtered", "top_discount");
            startActivity(intent);
        });

        tv_DisplayAllNewProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductActivity.class);
            intent.putExtra("filtered", "new_product");
            startActivity(intent);
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL){

                    performSearch(searchText.getText().toString());

                    // Clear text and hide keyboard
                    searchText.setText("");
                    hideKeyboard(mContext,v);
                    return true;
                }
                return false;
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on Notification", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        // load data when fragment is created
        loadData();
    }

    public void loadData() {
        getTotalCartItem();
        getTopProductsSelling();
        getTopProductsDiscount();
        getTopNewProducts();
        showSlider();
        getFeeds();
    }

    private void getTopProductsSelling(){
        ProductService productService = ServiceBuilder.buildService(ProductService.class);
        Call<List<Product>> request = productService.getBestSellerProducts();

        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()) {
                    productAdapter = new productAdapter(response.body(), HomeFragment.this, HomeFragment.this);

                    rcvTopProductSelling.setAdapter(productAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    rcvTopProductSelling.setLayoutManager(layoutManager);

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void getTopNewProducts(){
        ProductService productService = ServiceBuilder.buildService(ProductService.class);
        Call<List<Product>> request = productService.getNewProducts();

        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()) {
                    productAdapter = new productAdapter(response.body(), HomeFragment.this, HomeFragment.this);

                    rcvTopNewProduct.setAdapter(productAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    rcvTopNewProduct.setLayoutManager(layoutManager);

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getTotalCartItem(){
        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<Integer> request = cartService.getTotalItem();

        request.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    displayCartItemCount(response.body());
                } else if(response.code() == 401) {
                    displayCartItemCount(0);
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getTopProductsDiscount(){
        ProductService productService = ServiceBuilder.buildService(ProductService.class);
        Call<List<Product>> request = productService.getBestPromotionProducts();

        request.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productAdapter productDiscountAdapter = new productAdapter(response.body(), HomeFragment.this,HomeFragment.this);
                    rcvTopProductsDiscount.setAdapter(productDiscountAdapter);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    rcvTopProductsDiscount.setLayoutManager(layoutManager);
                } else if(response.code() == 401) {
                    Toast.makeText(mContext, "Your session has expired", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (t instanceof IOException){
                    Toast.makeText(mContext, "A connection error occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Failed to retrieve items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getFeeds(){
        new FetchFeedTask().execute((Void) null);
    }
    private void performSearch(String searchText) {
        Toast.makeText(mContext, searchText, Toast.LENGTH_LONG).show();

    }

    private void displayCartItemCount(int num) {
        if(num <= 0){
            redCircleCart.setVisibility(GONE);
        }
        else if(num > 99){
            tvNumberCart.setText("99");
            redCircleCart.setVisibility(VISIBLE);
        }
        else {
            tvNumberCart.setText(String.valueOf(num));
            redCircleCart.setVisibility(VISIBLE);
        }
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSlider(){
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel("https://data-service.pharmacity.io/pmc-ecm-webapp-config-api/production/banner/795%20x%20302%20(x2)%20(1)-1710388934651.png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240308081457-0-795%20x%20302%20%28x2%29.png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240305075612-0-PMCE_795x302(x2).png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240311085012-0-795x302%28x2%29%20%281%29.png", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://data-service.pharmacity.io/pmc-ecm-webapp-config-api/production/banner/795%20x%20302%20(x2)%20(1)-1710388934651.png", ScaleTypes.FIT));
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
         imageList.add(new SlideModel("https://prod-cdn.pharmacity.io/e-com/images/ecommerce/20240308084056-0-dealhot_dealhot_1590x604.jpg", ScaleTypes.FIT));

        imageSlider.setImageList(imageList,ScaleTypes.FIT);

    }


    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(getActivity(), DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) product);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onItemClick(Feed feed) {
        Intent intent = new Intent(getActivity(), HealthyNewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("url",  feed.getLink());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onButtonAddToCartClick(Product product) {
        showAddToCartDialog(product);
    }


    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {
        private String urlLink = "https://vnexpress.net/rss/suc-khoe.rss";

        private List<Feed> listNews;
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(urlLink).get();
                listNews = parseFeed(doc);
                return true;
            } catch (IOException e) {
                Log.e("TAG", "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Fill RecyclerView
                feedAdapter = new feedAdapter(listNews, HomeFragment.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                rcvFeeds.setAdapter(feedAdapter);
                rcvFeeds.setLayoutManager(linearLayoutManager);

            } else {

            }
        }
    }


    public static List<Feed> parseFeed(Document doc) {
        List<Feed> items = new ArrayList<>();
        Elements itemElements = doc.select("item");
        int count = Math.min(itemElements.size(), 5); // Giới hạn chỉ lấy 10 mục đầu tiên
        for (int i = 0; i < count; i++) {
            Element itemElement = itemElements.get(i);
            String title = itemElement.selectFirst("title").text();

            String description = itemElement.selectFirst("description").text();
            Document docc = Jsoup.parse(description);
            String extractedText = docc.text().trim();

            Element linkElement = itemElement.selectFirst("link");
            String link = linkElement.text();

            String pubDate = itemElement.selectFirst("pubDate").text();

            Element imgElement = itemElement.selectFirst("enclosure[url]");
            String img = "";
            if (imgElement != null) {
                img = imgElement.attr("url");
            }

            Feed feed = new Feed(title, link, extractedText, pubDate, img);
            items.add(feed);
        }

        return items;
    }


    private void showAddToCartDialog(Product product) {

        final AtomicInteger quantity = new AtomicInteger(1);
        final Dialog dialog = new Dialog(HomeFragment.this.getContext());


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_add_to_cart);

        // add control
        TextView tv_product_name = dialog.findViewById(R.id.tv_product_name);
        TextView tv_product_price = dialog.findViewById(R.id.tv_product_price);
        TextView tv_quantity = dialog.findViewById(R.id.tv_quantity);
        ImageView iv_product = dialog.findViewById(R.id.iv_product);
        LinearLayout ll_minus = dialog.findViewById(R.id.ll_minus);
        LinearLayout ll_plus = dialog.findViewById(R.id.ll_plus);
        ImageView iv_back = dialog.findViewById(R.id.iv_back);
        Button btn_AddToCart = dialog.findViewById(R.id.btn_AddToCart);
        Button btn_BuyNow = dialog.findViewById(R.id.btn_BuyNow);

        // add event
        iv_back.setOnClickListener(v -> dialog.dismiss());
        tv_product_name.setText(product.getName());
        tv_product_price.setText(Convert.convertPrice(product.getPrice()));
        Glide.with(dialog.getContext())
                .load(product.getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(iv_product);

        // set quantity minus
        ll_minus.setOnClickListener(v -> {
            if (quantity.get() > 1) {
                quantity.decrementAndGet();
                tv_quantity.setText(String.valueOf(quantity.get()));
            }
        });

        // set quantity plus
        ll_plus.setOnClickListener(v -> {
            quantity.incrementAndGet();
            tv_quantity.setText(String.valueOf(quantity.get()));
        });

        btn_AddToCart.setOnClickListener(v -> {
            Log.d("tag", "quantity: " +quantity.get());
            CartItem cartItem = new CartItem(product, quantity.get(), 1);
            addToCart(cartItem)
                    .thenAccept(result -> {
                        if (result == 200) {
                            // reset total cart
                            getTotalCartItem();

                            //get CartItems Checked from SharedPreferences
                            Type cartItemType = new TypeToken<List<CartItem>>() {}.getType();
                            List<CartItem> listCartItemsChecked = SharedPref.loadData(getContext(), Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED, cartItemType);
                            if(listCartItemsChecked == null){
                                listCartItemsChecked = new ArrayList<>();
                            }
                            listCartItemsChecked.add(cartItem);
                            // update CartItems Checked to SharedPreferences
                            SharedPref.saveData(getContext(), listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
                            Toast.makeText(mContext, "Add item to cart successfully", Toast.LENGTH_LONG).show();
                        }
                        else if(result == 401){
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Toast.makeText(mContext, "Failed to add item to cart", Toast.LENGTH_LONG).show();
                        }
                    })
                    .exceptionally(ex -> {
                        Log.e("Error", "Failed to add item to cart: " + ex.getMessage());
                        return null;
                    });
            dialog.dismiss();
        });


        // show dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private CompletableFuture<Integer> addToCart(CartItem cartItem) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        CartService cartService = ServiceBuilder.buildService(CartService.class);
        Call<CartItem> request = cartService.addCartItem(cartItem);

        request.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful()) {
                    int result = response.code();
                    future.complete(result);
                }
                else if(response.code() == 401){
                    int result = response.code();
                    future.complete(result);
                }else {
                    future.completeExceptionally(new Exception("Failed to add item to cart"));
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                if (t instanceof IOException) {
                    future.completeExceptionally(new Exception("A connection error occurred"));
                } else {
                    future.completeExceptionally(new Exception("Failed to add item to cart"));
                }
            }
        });

        return future;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isReloadData) {
            loadData();
            isReloadData = false;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        isReloadData = true;
    }
}
