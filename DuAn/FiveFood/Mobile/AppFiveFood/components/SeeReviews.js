import React, { useState, useEffect, useRef, useCallback } from 'react';
import { View, Text, Image, FlatList, Animated, TouchableOpacity, ScrollView } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import tw from 'twrnc';
import axios from 'axios';
import MenuTop from "./menu/MenuTop";
import Toast from "react-native-toast-message";
import className from 'twrnc';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {ApiUrl} from '../service/ApiUrl';
import { formatVND } from '../service/formatVND';
import { formatDate } from '../service/formatDate';
import { SafeAreaView } from 'react-native-safe-area-context';

const SeeReviews = ({ route }) => {
  const [quantity, setQuantity] = useState(1);
  const { maSanPham, check } = route.params;
  const [product, setProduct] = useState([], null);
  const [products, setProducts] = useState(null);
  const navigation = useNavigation();
  const [evaluates, setEvaluate] = useState([], null);
  const [currentPage, setCurrentPage] = useState(1); // Trạng thái trang hiện tại
  const [currentReviewPage, setCurrentReviewPage] = useState(1);
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
  const [count1Star, setCount1Star] = useState(0);
  const [count2Stars, setCount2Stars] = useState(0);
  const [count3Stars, setCount3Stars] = useState(0);
  const [count4Stars, setCount4Stars] = useState(0);
  const [count5Stars, setCount5Stars] = useState(0);

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
  }, [check, maSanPham, currentReviewPage]);

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
        ? `${ApiUrl}/api/danh-gia/page?page=1&pageSize=500&soSao=${rating}&maSanPham=${maSanPham}`
        : `${ApiUrl}/api/danh-gia/page/${page}?maSanPham=${maSanPham}`;
      const { data } = await axios.get(url);
      if (data && data.data && Array.isArray(data.data.danhGia)) {
        data.data.danhGia.forEach(review => fetchReviewImages(review.maDanhGia));
        const newReviews = data.data.danhGia;
        setEvaluate(newReviews);

        if (!initialRatingCounts) {
          setInitialRatingCounts(data.data.danhGia);
        }
        if (checkSoSaoDanhGia || initialRatingCounts === null) {
          const danhGias = data.data.danhGia;
          const avgRating = calculateAverageRating(danhGias);
          setInitialRatingCounts(data.data.danhGia);
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

          setCount1Star(counts[1]);
          setCount2Stars(counts[2]);
          setCount3Stars(counts[3]);
          setCount4Stars(counts[4]);
          setCount5Stars(counts[5]);
          const totalReviews = counts[1] + counts[2] + counts[3] + counts[4] + counts[5];
          setTotalAllStars(totalReviews);
        }
      } else {
        setEvaluate([]);
        setHasMoreReviews(false);
      }
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
  };

  const fetchReviewImages = async (reviewId) => {
    try {
      const response = await axios.get(`${ApiUrl}/api/hinh-anh-danh-gia/page/1?maDanhGia=${reviewId}`);
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

  const handleRatingFilter = (rating) => {
    setCheckSoSaoDanhGia(false);
    setEvaluate([]); // Clear current reviews
    setCurrentReviewPage(1); // Reset to first page
    fetchReviews(1, rating); // Fetch reviews based on selected rating
  };

  return (
    <SafeAreaView style={tw`flex-1 bg-[#F5F0DC]`} >
      {/* Header */}
      <MenuTop title='Đánh Giá' />
      <View style={tw`flex flex-row pl-4 pt-1`}>
        <Text style={tw`text-xl mt--1.5`}>{averageRating}</Text>
        <Text style={tw`text-xl text-[#FFA500]justify-center`}> {renderStars(parseFloat(averageRating))}</Text>
      </View>
      <View style={tw`flex flex-row px-4 pb-2`}>
        <TouchableOpacity
          style={tw`bg-white py-2 rounded-lg mr-1 px-1 flex flex-row`}
          onPress={() => handleRatingFilter('')}
        >
          <Text style={tw`text-2.5`}>Tất Cả({totalAllStars})</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={tw`bg-white py-2 rounded-lg mr-1 px-1 flex flex-row`}
          onPress={() => handleRatingFilter('5')}
        >
          <Text style={tw`text-2.5`}>5 sao({count5Stars})</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={tw`bg-white py-2 rounded-lg mr-1 px-1 flex flex-row`}
          onPress={() => handleRatingFilter('4')}
        >
          <Text style={tw`text-2.5`}>4 sao({count4Stars})</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={tw`bg-white py-2 rounded-lg mr-1 px-1 flex flex-row`}
          onPress={() => handleRatingFilter('3')}
        >
          <Text style={tw`text-2.5`}>3 sao({count3Stars})</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={tw`bg-white py-2 rounded-lg mr-1 px-1 flex flex-row`}
          onPress={() => handleRatingFilter('2')}
        >
          <Text style={tw`text-2.5`}>2 sao({count2Stars})</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={tw`bg-white py-1.5 rounded-lg mr-1 px-1 flex flex-row`}
          onPress={() => handleRatingFilter('1')}
        >
          <Text style={tw`text-2.5`}>1 sao({count1Star})</Text>
        </TouchableOpacity>
      </View>

      <ScrollView ref={scrollViewRef} showsVerticalScrollIndicator={false} style={tw`px-4 pb-4`}>
        <FlatList
          data={evaluates}
          keyExtractor={(item) => item.maDanhGia}
          renderItem={({ item }) => (
            <View style={tw`p-4 bg-white shadow-sm rounded-md mb-1`}>
              <View style={tw`flex-row items-center`}>
                <Image
                  source={{ uri: item.taiKhoanDG.hinhAnh }}
                  style={tw`w-9 h-9 rounded-full`}
                />
                <View style={tw`ml-2`}>
                  <Text style={tw`text-2.8`}>{item.taiKhoanDG.hoVaTen}</Text>
                  <FlatList
                    data={Array(item.soSao).fill(0)}
                    horizontal
                    renderItem={(_, index) => (
                      <Icon name="star" size={12} color="gold" key={`review-star-${index}`} />
                    )}
                    keyExtractor={(_, index) => `review-star-${index}`}
                  />
                  <Text style={tw`text-2 text-gray-500`}>{formatDate(item.ngayDanhGia)}</Text>
                </View>
              </View>
              <Text style={tw`text-sm text-gray-500 mt-2`}>{item.binhLuan}</Text>
              <FlatList
                data={reviewImages.filter((img) => img.maDanhGia === item.maDanhGia)}
                horizontal
                keyExtractor={(img) => img.tenHinhAnh}
                renderItem={({ item }) => (
                  <Image
                    source={{ uri: item.tenHinhAnh }}
                    style={tw`w-20 h-20 mx-1 rounded`}
                    resizeMode="cover"
                  />
                )}
              />
              {item.taiKhoanDG.maTaiKhoan === taiKhoan.maTaiKhoan && (
                <View style={tw`absolute bottom-2 right-2 bg-white p-2 rounded-md`}>
                  <TouchableOpacity onPress={() =>
                    navigation.navigate("EditEvaluate", { maDanhGia: item.maDanhGia, maSanPham: item.maSanPham })
                  }>
                    <Text style={tw`font-bold text-blue-500`}>Sửa</Text>
                  </TouchableOpacity>
                </View>
              )}
            </View>
          )}
        />
      </ScrollView>
    </SafeAreaView>
  );
};

export default SeeReviews;
