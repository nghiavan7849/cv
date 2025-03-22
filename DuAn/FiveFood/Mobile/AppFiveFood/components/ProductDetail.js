import React, { useState, useEffect, useRef, useCallback } from 'react';
import { View, Text, Image, FlatList, Animated, TouchableOpacity, ScrollView, SafeAreaView } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import tw from 'twrnc';
import axios from 'axios';
import MenuTop from "./menu/MenuTop";
import Toast from "react-native-toast-message";
import className from 'twrnc';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { ApiUrl } from '../service/ApiUrl';
import { formatVND } from '../service/formatVND';
import { formatDate } from '../service/formatDate';

const ProductDetail = ({ route }) => {
  const [quantity, setQuantity] = useState(1);
  const { maSanPham, check } = route.params;
  const [product, setProduct] = useState([], null);
  const [products, setProducts] = useState(null);
  const navigation = useNavigation();
  const [evaluates, setEvaluate] = useState([], null);
  const [currentPage, setCurrentPage] = useState(1); // Trạng thái trang hiện tại
  const [currentReviewPage, setCurrentReviewPage] = useState(1);
  const [hasMoreProducts, setHasMoreProducts] = useState(true);
  const [hasMoreReviews, setHasMoreReviews] = useState(true);
  const [averageRating, setAverageRating] = useState(0);
  const [totalAllStars, setTotalAllStars] = useState(0);
  const [reviewImages, setReviewImages] = useState([]);
  const [loggedInUserId, setLoggedInUserId] = useState(null);
  const [initialRatingCounts, setInitialRatingCounts] = useState(null); // Lưu chỉ số ban đầu
  const [checkSoSaoDanhGia, setCheckSoSaoDanhGia] = useState(true);
  const [productImages, setProductImages] = useState([]);
  const [taiKhoan, setTaiKhoan] = useState({});
  const scrollViewRef = useRef();
  const fadeAnim = useRef(new Animated.Value(0)).current;
  const translateAnim = useRef(new Animated.Value(10)).current;
  const [token, setToken] = useState('');
  const [currentStarFilter, setCurrentStarFilter] = useState('');
  const filteredEvaluates = currentStarFilter
    ? evaluates.filter(eva => eva.soSao === currentStarFilter)
    : evaluates;

  const loadData = async () => {
    try {
      let sessionTaiKhoanStored = await AsyncStorage.getItem('sessionTaiKhoan');
      const sessionToken = await AsyncStorage.getItem('sessionTokenTK');
      console.log('sessionToken', sessionToken)
      const storedProduct = await AsyncStorage.getItem('currentProduct');
      if (averageRating !== null) {
        setAverageRating(JSON.parse(averageRating));
      }
      if (totalAllStars !== null) {
        setTotalAllStars(JSON.parse(totalAllStars));
      }
      if (sessionTaiKhoanStored) {
        setToken(sessionToken);
        const sessionTaiKhoan = JSON.parse(sessionTaiKhoanStored);
        setTaiKhoan(sessionTaiKhoan);
      } else {
        setTaiKhoan("");
      }
      if (storedProduct) {
        const products = JSON.parse(storedProduct);
        setProduct(products);
        setAverageRating(products.averageRating || 0);
        setTotalAllStars(products.totalAllStars || 0);
      }
      setToken(sessionToken);
      await fetchProductDetails(maSanPham);
      await fetchReviews(currentReviewPage, "", token);
      if (scrollViewRef.current) {
        scrollViewRef.current.scrollTo({ y: 0, animated: true });
      }
    } catch (error) {
      console.error("Error loading data:", error);
    }
    return () => { };
  };

  useEffect(() => {
    loadData();
    // console.log('test');
    // console.log(evaluates);
  }, [check, maSanPham, currentReviewPage]);

  // Làm mới dữ liệu khi màn hình được focus


  const fetchProductDetails = async (maSanPham) => {
    try {
      const response = await axios.get(`${ApiUrl}/api/san-pham/get-one?maSanPham=${maSanPham}`);
      const data = response.data;
      if (data && data.data) {
        const productData = data.data.sanPham;
        setProduct(productData);
        setAverageRating(data.data.averageRating || 0);
        setTotalAllStars(data.data.totalAllStars || 0);
        await AsyncStorage.setItem('currentProduct', JSON.stringify(productData));
        // setProductImages(imageResponse.data.data || []);
        fetchSimilarProducts(productData.loaiSP?.tenLoai || '');
      }
    } catch (error) {
      console.error('Error fetching product details:', error);
    }
  };

  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <Icon
          key={i}
          name={i <= rating ? "star" : i - rating < 1 ? "star-half" : "star-border"}
          size={24}
          color="#FFD700"
        />
      );
    }
    return stars;
  };

  const fetchReviews = async (page, rating = '') => {
    try {
      const url = rating
        ? `${ApiUrl}/api/danh-gia/page?page=1&pageSize=5&soSao=${rating}&maSanPham=${maSanPham}`
        : `${ApiUrl}/api/danh-gia/page/${page}?maSanPham=${maSanPham}`;
      // console.log(url);

      const { data } = await axios.get(url);
      if (data && data.data && Array.isArray(data.data.danhGia)) {
        data.data.danhGia.forEach(review => fetchReviewImages(review.maDanhGia));
        const newReviews = data.data.danhGia;
        setEvaluate(newReviews);

        if (!initialRatingCounts) {
          setInitialRatingCounts(data.data.danhGia);
        }
        const danhGias = data.data.danhGia;
        const avgRating = calculateAverageRating(danhGias);
        const formattedRating = avgRating.toFixed(1);
        setAverageRating(parseFloat(formattedRating));

        const counts = data.data.danhGia.reduce((acc, review) => {
          if (review.soSao === 1) acc[1]++;
          if (review.soSao === 2) acc[2]++;
          if (review.soSao === 3) acc[3]++;
          if (review.soSao === 4) acc[4]++;
          if (review.soSao === 5) acc[5]++;
          return acc;
        }, { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 });
        const totalReviews = counts[1] + counts[2] + counts[3] + counts[4] + counts[5];
        // console.log('totalReviews', totalReviews)
        setTotalAllStars(totalReviews);
        setHasMoreReviews(newReviews.length > 0);
      }
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
  };

  const fetchReviewImages = async (reviewId) => {
    try {
      const response = await axios.get(`${ApiUrl}/api/hinh-anh-danh-gia/page/1?maDanhGia=${reviewId}`, {});
      const data = response.data;
      if (data && data.data && Array.isArray(data.data.hinhAnhDG)) {
        const imagePromises = data.data.hinhAnhDG.map(async (image) => {
          return {
            maDanhGia: image.danhGia.maDanhGia,
            tenHinhAnh: image.tenHinhAnh,
            src: image.tenHinhAnh,
          };
        });
        const newImages = await Promise.all(imagePromises);
        setReviewImages((prev) => {
          const allImages = [...prev, ...newImages];
          const uniqueImages = Array.from(new Set(allImages.map(img => img.src)))
            .map(src => allImages.find(img => img.src === src));
          return uniqueImages;
        });
      }
    } catch (error) {
      console.error("Failed to fetch review images from Firebase:", error);
    }
  };
  const calculateAverageRating = (danhGias) => {
    if (danhGias.length === 0) return 0;
    // Tính tổng số sao
    const totalStars = danhGias.reduce((acc, review) => acc + review.soSao, 0);
    // Tính trung bình số sao
    return totalStars / danhGias.length;
  };

  const loadMoreReviews = () => {
    setCheckSoSaoDanhGia(true);
    if (hasMoreReviews) {
      setCurrentReviewPage(prevPage => prevPage + 1);
    }
  };

  const loadMoreProducts = () => {
    if (hasMoreProducts) {
      setCurrentPage(prevPage => prevPage + 1);
      fetchSimilarProducts(currentPage + 1);
    }
  };
  const fetchSimilarProducts = async (tenLoai) => {
    try {
      const { data } = await axios.get(`${ApiUrl}/api/san-pham/get-list-by-bo-loc?timKiem=${tenLoai}&page=1&pageSize=50`);
      setProducts(data.data.sanPham || []);
    } catch (error) {
      console.error('Error fetching similar products:', error);
    }
  };

  const handleRatingFilter = (rating) => {
    setCheckSoSaoDanhGia(false);
    setEvaluate([]); // Clear current reviews
    setCurrentReviewPage(1); // Reset to first page
    fetchReviews(1, rating); // Fetch reviews based on selected rating
  };

  const decrementQuantity = () => {
    if (quantity > 1) setQuantity(prevQuantity => prevQuantity - 1);
  };

  const incrementQuantity = () =>{
    if(quantity < 10){
      setQuantity(prevQuantity => prevQuantity + 1);
    }
  }

  useEffect(() => {
    Animated.timing(fadeAnim, {
      toValue: 1,
      duration: 1000,
      useNativeDriver: true,
    }).start();

    Animated.timing(translateAnim, {
      toValue: 0,
      duration: 1000,
      useNativeDriver: true,
    }).start();
  }, []);

  const createGioHang = (data, token) => {
    // console.log("api: " + token);
    // console.log(data)
    return axios.post(`${ApiUrl}/api/chi-tiet-gio-hang/create`, data, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  };

  const clickPay = async (item) => {
    if (taiKhoan && token) {
      let maTaiKhoan = taiKhoan.maTaiKhoan;
      try {
        let res = await createGioHang({
          "soLuong": quantity,
          "maTaiKhoan": maTaiKhoan,
          "maSanPham": product.maSanPham
        }, token);

        if (res) {
          Toast.show({
            type: 'success',
            text1: 'Thêm vào giỏ hàng thành công',
            text1Style: className`text-lg`

          });
        }
      } catch (error) {
        console.error('Error creating cart:', error);
      }
    } else {
      navigation.navigate('Login', { state: { form: location } });
    }
  };

  const clickDatHang = () => {
    const data = {
      maChiTietGioHang: Math.floor(new Date().getTime() / 1000),
      sanPhamGH: product,
      soLuong: quantity,
      taiKhoanGH: taiKhoan
    }
    navigation.navigate('PayDetail', { listCartClick: [data] });
    // navigate("/home/pay-details", { state: [data] });
  }

  if (!products) {
    return <Text style={tw`mt-100 ml-35 text-5`}>Loading...</Text>;
  }

  return (
    <SafeAreaView style={tw`flex-1 bg-[#F5F0DC]`} >
      {/* Header */}
      <MenuTop />
      <ScrollView ref={scrollViewRef} showsVerticalScrollIndicator={false}>
        {/* Product Image and Details */}
        <View style={tw`rounded-lg`}>
          {/* Image Column */}
          <Image
            source={{ uri: product.hinhAnh }}
            style={tw`w-full h-90 mb-4`}
          />
          {/* Product Info Column */}
          <View style={tw`pl-2.5 flex-1 justify-between px-4`}>
            <Text style={tw`text-5 font-bold mb-1.25`}>{product.tenSanPham}</Text>
            <View style={tw`flex flex-row items-center mb-2.5 justify-between`}>
              <Text style={tw`text-xl text-red-500 mt-1`}>{formatVND(product.gia)}</Text>
              <View style={tw`flex flex-row items-center mr-5 mt-5`}>
                <TouchableOpacity onPress={decrementQuantity} style={tw`p-1.25 border border-[#ddd] rounded-lg`}>
                  <Icon name="remove" size={12} color="#000" />
                </TouchableOpacity>
                <Text style={tw`mx-2.5 text-4`}>{quantity}</Text>
                <TouchableOpacity onPress={incrementQuantity} style={tw`p-1.25 border border-[#ddd] rounded-lg`}>
                  <Icon name="add" size={12} color="#000" />
                </TouchableOpacity>
              </View>
            </View>
            <Text>{product.moTa}</Text>
            <Text style={tw`text-sm text-[#888] mt-2.5`}>Giao hàng trong vòng 30 phút</Text>
          </View>
        </View>

        <View style={tw`p-4 `}>
          {/* Product Reviews */}
          <Text style={tw`text-xl font-semibold mb-2.5`}>Đánh Giá ({totalAllStars})</Text>
          <View style={tw`flex flex-row items-center justify-between`}>
            <View style={tw`flex flex-row`}>
              <Text style={tw`text-xl`}>{averageRating}</Text>
              <Text style={tw`text-xl mt-1 text-[#FFA500] justify-center`}> {renderStars(parseFloat(averageRating))}</Text>
            </View>
            <Text style={tw``}
              onPress={() => navigation.navigate('SeeReviews', { maSanPham: maSanPham })}>Xem thêm</Text>
          </View>
        </View>
        <View style={tw`flex-1 p-2`}>
          <Text style={tw`text-xl font-semibold mb-2.5 mt-2.5 pl-2`}>Sản Phẩm Tương Tự</Text>
          {products.length > 0 ? (
            <FlatList
              scrollEnabled={false}
              data={products.filter((item) => item.trangThai === true && item.maSanPham !== product.maSanPham )}
              keyExtractor={(item) => item.maSanPham.toString()}
              numColumns={2}
              columnWrapperStyle={tw`justify-between`}
              showsVerticalScrollIndicator={false}
              renderItem={({ item }) => (
                <TouchableOpacity
                  key={item.maSanPham.toString()}
                  style={tw`w-1/2 p-2`}
                  onPress={() => navigation.navigate('ProductDetail', { maSanPham: item.maSanPham })}
                >
                  {/* Hình ảnh sản phẩm */}
                  <Image
                    source={{ uri: item.hinhAnh }}
                    style={tw`w-full h-40 rounded-lg`}
                  />
                  {/* Tên sản phẩm */}
                  <Text
                    style={tw`text-left font-semibold text-black mt-2`}
                  >
                    {item.tenSanPham}
                  </Text>
                  {/* Giá và nút thêm */}
                  <View style={tw`flex-row justify-between items-center`}>
                    <Text style={tw`text-red-500 font-semibold text-base`}>
                      {formatVND(item.gia)}
                    </Text>
                    <TouchableOpacity onPress={() => clickPay(item.maSanPham)}>
                      <Text style={tw`w-8 h-8 bg-green-500 text-white mt-1 justify-center text-center rounded-full text-lg leading-6`}>
                        +
                      </Text>
                    </TouchableOpacity>
                  </View>
                </TouchableOpacity>
              )}
            />
          ) : (
            <View>
              <Text>Không có sản phẩm để hiển thị.</Text>
            </View>
          )}
        </View>
      </ScrollView>
      <View style={tw`flex-row justify-between ml-3 mr-3 mt-1 pt-2 pb-2`}>
        <TouchableOpacity
          style={tw`border-2 border-green-500 py-2 rounded-lg mr-3 px-3 flex-1 bg-transparent`}
          onPress={() => clickPay()}
        >
          <Text style={tw`text-black font-bold text-center`}>Thêm vào giỏ hàng</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={tw`bg-[#28a745] py-2 px-3 flex-1 rounded-lg justify-center`}
          onPress={() => clickDatHang()}
        >
          <Text style={tw`text-white font-bold text-center`}>Mua ngay</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView >
  );
};

export default ProductDetail;
