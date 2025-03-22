import React, { useEffect, useState, useRef, useCallback } from "react";
import {
  View,
  Text,
  TextInput,
  FlatList,
  Animated,
  Image,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Platform,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import axios from "axios";
import BellIcon from "../assets/images/BellIcon";
import categories from "../assets/data/categories.json";
import className, { style } from "twrnc";
import { formatVND } from "../service/formatVND";
import { useFocusEffect, useNavigation } from "@react-navigation/native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { ApiUrl } from "../service/ApiUrl";
import Toast from "react-native-toast-message";
import * as ImagePicker from "expo-image-picker";
import tw from "twrnc";
import { SafeAreaView } from "react-native-safe-area-context";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { flaskUrl } from "../service/ApiUrl";
const Tab = createBottomTabNavigator();

const createGioHang = (data, token) => {
  console.log("api: " + token);
  console.log(data);
  return axios.post(`${ApiUrl}/api/chi-tiet-gio-hang/create`, data, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const getProductsByPage = (page, pageSize) => {
  return axios.get(
    `${ApiUrl}/api/san-pham/page?page=${page}&pageSize=${pageSize}`,
    {}
  );
};

const HomeScreen = ({ navigation }) => {
  const [products, setProducts] = useState([]);
  const [allProducts, setAllProducts] = useState([]);
  const [foundProduct, setFoundProduct] = useState("NULL");
  const [taiKhoan, setTaiKhoan] = useState({});
  const fadeAnim = useRef(new Animated.Value(0)).current;
  const translateAnim = useRef(new Animated.Value(10)).current;
  const [filteredCategories, setFilteredCategories] = useState(categories);
  const [hoVaTen, setHoVaTen] = useState("Phát");
  const [loading, setLoading] = useState(true);
  const [totalPage, setTotalPages] = useState(0);
  const [boLoc, setBoLoc] = useState("");
  const [valueSapXep, setValueSapXep] = useState("");
  // const shuffledProducts = shuffleArray([...products]);
  const flatListRef = useRef(null);
  const scrollViewRef = useRef();
  const [searchQuery, setSearchQuery] = useState("");
  const [page, setPage] = useState(1);
  const [imageUri, setImageUri] = useState(null);
  const [pageSize, setPageSize] = useState(20);
  const [token, setToken] = useState("");
  const [predictions, setPredictions] = useState([]); // Kết quả dự đoán
  const [model, setModel] = useState(null); // Model đã tải

  useFocusEffect(
    useCallback(() => {
      const fetchSessionTaiKhoan = async () => {
        try {
          const sessionTaiKhoanStored = await AsyncStorage.getItem(
            "sessionTaiKhoan"
          );
          const sessionToken = await AsyncStorage.getItem("sessionTokenTK");
          if (sessionTaiKhoanStored) {
            setToken(sessionToken);
            const sessionTaiKhoan = JSON.parse(sessionTaiKhoanStored);
            setTaiKhoan(sessionTaiKhoan);
          } else {
            setTaiKhoan("");
          }
        } catch (error) {
          console.error("Lỗi khi lấy sessionTaiKhoan:", error);
        }
      };
      setToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7InRhaUtob2FuIjp7Im1hVGFpS2hvYW4iOjYsInRlbkRhbmdOaGFwIjoidXNlciIsImVtYWlsIjoidXNlckBmcHQuZWR1LnZuIiwiaG9WYVRlbiI6IlVzZXIiLCJzb0RpZW5UaG9haSI6IjA3MDIxMTQ5MzciLCJnaW9pVGluaCI6dHJ1ZSwiaGluaEFuaCI6Imh0dHBzOi8vZmlyZWJhc2VzdG9yYWdlLmdvb2dsZWFwaXMuY29tL3YwL2IvZml2ZWZvb2QtZGF0bi04YTFjZi5hcHBzcG90LmNvbS9vL2F2YXRhcnMlMkZ0cmlldW5tLnBuZz9hbHQ9bWVkaWEmdG9rZW49NmQ3OTNmMzItMzMwZC00YzMxLTg5MjItY2I5YTFlNjE4NDNiIiwidHJhbmdUaGFpIjp0cnVlLCJ2YWlUcm8iOmZhbHNlfX0sInN1YiI6InVzZXIiLCJpYXQiOjE3MzExMzU2MDIsImV4cCI6MTczMTEzNzQwMn0.JqHZRCLhFB4UwnvCE88EWhhWiGuLxMjaDSk1dMTv1PI"
      );
      fetchSessionTaiKhoan();
      return () => {};
    }, [])
  );

  const getAllProducts = async () => {
    const response = await axios.get(`${ApiUrl}/api/san-pham/list`);
    setAllProducts(response.data.data.sanPham);
  };

  const fetchProducts = async (page = 1, pageSize = 100) => {
    try {
      let res = await getProductsByPage(page, pageSize);
      if (res.data.status && res.data.data) {
        setProducts(res.data.data.sanPham || []);
        setTotalPages(res.data.totalPage || 1);
      } else {
        setProducts([]);
      }
    } catch (error) {
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (searchQuery === "") {
      fetchProducts();
    } else {
      const filtered = products.filter((product) =>
        product.tenSanPham.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setProducts(filtered);
    }
  }, [searchQuery]);

  const clickPay = async (item) => {
    // Get session data from AsyncStorage
    if (taiKhoan && token) {
      let maTaiKhoan = taiKhoan.maTaiKhoan;
      try {
        let res = await createGioHang(
          {
            soLuong: 1,
            maTaiKhoan: maTaiKhoan,
            maSanPham: item.maSanPham,
          },
          token
        );

        if (res) {
          Toast.show({
            type: "success",
            text1: "Thêm vào giỏ hàng thành công",
            text1Style: className`text-lg`,
          });
        }
      } catch (error) {
        console.error("Error creating cart:", error);
      }
    } else {
      navigation.navigate("Login", { state: { form: location } });
    }
  };

  const scrollToTop = () => {
    if (scrollViewRef.current) {
      scrollViewRef.current.scrollTo({ y: 0, animated: true });
    }
  };
  const goToNextPage = () => {
    if (loading) return; // Tránh việc gọi API khi đang tải
    const nextPage = page + 1; // Tăng trang lên 1
    setPage(nextPage); // Cập nhật trang hiện tại
    fetchProducts(nextPage, pageSize); // Gọi lại API với trang mới
    scrollToTop();
  };
  // Hàm để gọi khi nhấn nút Prev
  const goToPrevPage = () => {
    if (loading || page <= 1) return; // Tránh việc gọi API khi đang tải hoặc khi trang là 1
    const prevPage = page - 1; // Giảm trang xuống 1
    setPage(prevPage); // Cập nhật trang hiện tại
    fetchProducts(prevPage, pageSize); // Gọi lại API với trang mới
    scrollToTop();
  };

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
    getAllProducts();
  }, []);

  // Hàm pickImage chỉ chọn 1 ảnh duy nhất
  const pickImage = async () => {
    const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
    if (status !== "granted") {
      alert("Ứng dụng cần quyền truy cập thư viện ảnh để tiếp tục.");
      return;
    }

    // Mở thư viện ảnh
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images, // Chỉ chọn ảnh
      allowsEditing: true, // Cho phép chỉnh sửa ảnh
      aspect: [4, 3], // Tỉ lệ cắt ảnh (tuỳ chọn)
      quality: 1, // Chất lượng ảnh
    });
  };

  const getLastName = (fullName) => {
    return fullName ? fullName.split(" ").pop() : "";
  };

  const handleSearchWithImage = async (media) => {
    let status;
    let response;

    if (media === "camera") {
      status = await ImagePicker.requestCameraPermissionsAsync();
      // Kiểm tra quyền truy cập
      if (status.status !== "granted") {
        console.log("Cần quyền truy cập vào thư viện ảnh hoặc camera");
        return;
      }
      response = await ImagePicker.launchCameraAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: false,
        quality: 1,
      });
    } else if (media === "library") {
      status = await ImagePicker.requestMediaLibraryPermissionsAsync();
      if (status.status !== "granted") {
        console.log("Cần quyền truy cập vào thư viện ảnh hoặc camera");
        return;
      }
      response = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: false,
        quality: 1,
      });
    }

    if (response.canceled) {
      console.log("Hủy hành động");
    } else if (response.assets) {
      const selectedUri = response.assets[0].uri;
      console.log("Đã nhận dữ liệu hình ảnh:", selectedUri);
      setImageUri(selectedUri); // Lưu URI ảnh
    }

    // if (response.canceled) {
    //   console.log("Tắt thư viện ảnh");
    // }else if(response.assets){
    //   const selectedUri = response.assets[0].uri; // Lưu URI ảnh
    //   setImageUri(selectedUri);

    // } else {
    //   console.log("Có lỗi xảy ra khi chọn ảnh");
    // }
  };

  useEffect(() => {
    if (imageUri) {
      sendImageToBackend(imageUri);
    }
  }, [imageUri]);

  const sendImageToBackend = async (imageUri) => {
    if (!imageUri) {
      console.error("Không có URI ảnh để gửi!");
      return;
    }

    const formData = new FormData();
    formData.append("file", {
      uri: imageUri,
      name: "image.jpg",
      type: "image/jpeg", // Đảm bảo loại MIME chính xác
    });

    try {
      const response = await axios.post(`${flaskUrl}/callModel`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("Prediction result:", response.data.tenSanPham);
      setFoundProduct(response.data.tenSanPham);
    } catch (error) {
      console.error("Error sending image:", error.message);
    }
  };

  return (
    <SafeAreaView style={tw`flex-1 bg-[#F5F0DC]`}>
      {/* Greeting Section */}
      <View style={tw`flex-row items-center mt-3 ml-2.5 mr-2.5`}>
        <TouchableOpacity onPress={() => navigation.navigate("AccountInfo")}>
          {taiKhoan && taiKhoan.hinhAnh ? (
            <Image
              style={tw`mx-2.5 rounded-full`}
              source={{ uri: taiKhoan.hinhAnh }}
              alt={taiKhoan.hoVaTen || "Unknown Author"}
              width={40}
              height={40}
            />
          ) : (
            <Icon name="person-circle-outline" size={40} />
          )}
        </TouchableOpacity>

        <View>
          <Text style={tw`text-sm font-bold`}>
            {taiKhoan && taiKhoan.hoVaTen == null ? (
              <Text style={tw`text-sm font-bold`}>Xin chào!</Text>
            ) : (
              <Text style={tw`text-sm font-bold`}>
                Xin chào, {taiKhoan ? getLastName(taiKhoan.hoVaTen) : ""}!
              </Text>
            )}
          </Text>
          <Text>Đặt món đi nào!</Text>
        </View>
        <Icon size={30} style={tw`ml-auto`} />
        <View name="ellipsis-horizontal-circle" size={30}>
          <BellIcon />
        </View>
      </View>

      <ScrollView
        style={className`mt-2 pt-2`}
        showsVerticalScrollIndicator={false}
        showsHorizontalScrollIndicator={false}
      >
        {/* Search Bar */}
        <View
          style={tw`flex flex-row items-center bg-gray-200 rounded-lg px-2.5 mx-4 mb-2.5`}
        >
          <TouchableOpacity>
            <Icon name="search-outline" style={tw`text-3xl`} />
          </TouchableOpacity>
          <TextInput
            placeholder="Tìm nhanh món ăn, thức uống..."
            value={searchQuery}
            onChangeText={(text) => setSearchQuery(text)}
            style={tw`flex-1 p-3 mb-2 `}
          />
          <TouchableOpacity
            style={className`mx-3`}
            onPress={() => handleSearchWithImage("camera")}
          >
            <Icon name="camera-sharp" size={20} />
          </TouchableOpacity>
          <TouchableOpacity onPress={() => handleSearchWithImage("library")}>
            <Icon name="image" size={20} />
          </TouchableOpacity>
          {/* <TouchableOpacity>
            <Icon name="mic-outline" style={tw`text-3xl mr-1`} />
          </TouchableOpacity>
          <TouchableOpacity onPress={pickImage}>
            <Icon name="camera-outline" style={tw`text-3xl`} />
          </TouchableOpacity> */}
        </View>

        {/* Promotional Banner */}
        <View style={tw`px-4 my-2.5`}>
          <Image
            source={{
              uri: "https://inan2h.vn/wp-content/uploads/2022/12/in-banner-quang-cao-do-an-4-1.jpg",
            }} // Replace with actual banner image URL
            style={tw`w-full h-37.5 rounded-lg`}
          />
        </View>

        {/* Tìm kiếm bằng hình ảnh */}
        {imageUri ? (
          <Animated.View
            style={{
              opacity: fadeAnim,
              transform: [{ translateY: translateAnim }],
              marginLeft: 14,
            }}
          >
            <Text style={tw`text-lg font-bold pl-2 mb-2`}>
              Sản phẩm tương tự
            </Text>
            <FlatList
              horizontal
              data={allProducts.filter(
                (item) =>
                  item.trangThai === true &&
                  item.tenSanPham.toLowerCase().includes(foundProduct.toLowerCase())
              )}
              keyExtractor={(item) => item.maSanPham.toString()}
              showsHorizontalScrollIndicator={false}
              renderItem={({ item }) => (
                <TouchableOpacity
                  key={item.maSanPham.toString()}
                  style={tw`w-40 p-2`}
                  onPress={() =>
                    navigation.navigate("ProductDetail", {
                      maSanPham: item.maSanPham,
                    })
                  }
                >
                  {/* Hình ảnh sản phẩm */}
                  <Image
                    source={{ uri: item.hinhAnh }}
                    style={tw`w-full h-40 rounded-lg`}
                  />
                  {/* Tên sản phẩm */}
                  <Text
                    style={tw`text-left font-semibold text-black mt-1`}
                    numberOfLines={1}
                    ellipsizeMode="tail"
                  >
                    {item.tenSanPham}
                  </Text>
                  {/* Giá và nút thêm */}
                  <View style={tw`flex-row justify-between items-center mt-1`}>
                    <Text style={tw`text-red-500 font-semibold text-base`}>
                      {formatVND(item.gia)}
                    </Text>
                    <TouchableOpacity onPress={() => clickPay(item)}>
                      <Text
                        style={tw`w-8 h-8 bg-green-500 text-white justify-center text-center rounded-full text-lg leading-6`}
                      >
                        +
                      </Text>
                    </TouchableOpacity>
                  </View>
                </TouchableOpacity>
              )}
            />
          </Animated.View>
        ) : (
          <></>
        )}
        <View style={tw`flex-1 p-2`}>
          <Text style={tw`text-lg font-bold pl-2`}>
            Danh sách món ăn, thức uống
          </Text>
          <FlatList
            ref={flatListRef}
            scrollEnabled={false}
            data={products.filter((item) => item.trangThai === true)}
            keyExtractor={(item) => item.maSanPham.toString()}
            numColumns={2}
            columnWrapperStyle={tw`justify-between`}
            showsVerticalScrollIndicator={false}
            renderItem={({ item }) => (
              <TouchableOpacity
                key={item.maSanPham.toString()}
                style={tw`w-1/2 p-2`}
                onPress={() =>
                  navigation.navigate("ProductDetail", {
                    maSanPham: item.maSanPham,
                  })
                }
              >
                {/* Hình ảnh sản phẩm */}
                <Image
                  source={{ uri: item.hinhAnh }}
                  style={tw`w-full h-40 rounded-lg`}
                />
                {/* Tên sản phẩm */}
                <Text
                  style={tw`text-left font-semibold text-black`}
                  numberOfLines={2}
                  ellipsizeMode="tail"
                >
                  {item.tenSanPham}
                </Text>
                {/* Giá và nút thêm */}
                <View style={tw`flex-row justify-between items-center`}>
                  <Text style={tw`text-red-500 font-semibold text-base`}>
                    {formatVND(item.gia)}
                  </Text>
                  <TouchableOpacity onPress={() => clickPay(item)}>
                    <Text
                      style={tw`w-8 h-8 bg-green-500 text-white mt-1 justify-center text-center rounded-full text-lg leading-6`}
                    >
                      +
                    </Text>
                  </TouchableOpacity>
                </View>
              </TouchableOpacity>
            )}
          />
          {/* Các nút Prev và Next
          <View style={tw`flex flex-row justify-center mt-2 items-center`}>
            <TouchableOpacity
              style={tw`bg-green-500 px-4 py-2 mx-2 rounded disabled:opacity-50`}
              onPress={goToPrevPage}
              disabled={page === 1}
            >
              <Text style={tw`text-white font-semibold`}>Prev</Text>
            </TouchableOpacity>
            <View style={tw`flex flex-row justify-center items-center`}>
              <Text style={tw`text-lg mx-2 text-gray-600`}>{page - 1}</Text>
              <Text style={tw`text-lg mx-2 font-bold text-black`}>{page}</Text>
              <Text style={tw`text-lg mx-2 text-gray-600`}>{page + 1}</Text>
            </View>
            <TouchableOpacity
              style={tw`bg-green-500 px-4 py-2 mx-2 rounded disabled:opacity-50`}
              onPress={goToNextPage}
              disabled={page === totalPage}
            >
              <Text style={tw`text-white font-semibold`}>Next</Text>
            </TouchableOpacity>
          </View> */}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F5F0DC",
    paddingTop: Platform.OS === "android" ? 50 : 0,
  },
  greetingSection: {
    flexDirection: "row",
    alignItems: "center",
    marginLeft: 10,
    marginRight: 10,
    marginBottom: 10,
  },
  greetingText: {
    fontSize: 13,
    fontWeight: "bold",
  },
  menuIcon: {
    marginLeft: "auto",
  },
  searchBar: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#f0f0f0",
    borderRadius: 8,
    paddingHorizontal: 10,
    marginHorizontal: 16,
    marginVertical: 10,
  },
  searchInput: {
    flex: 1,
    paddingVertical: 8,
    marginLeft: 5,
    height: 30,
  },
  bannerContainer: {
    paddingHorizontal: 16,
    marginVertical: 10,
  },
  bannerImage: {
    width: "100%",
    height: 150,
    borderRadius: 8,
  },
  productBanChay: {
    flex: 1,
    marginBottom: 15,
    backgroundColor: "#FFF",
    marginRight: 5,
    paddingHorizontal: -32,
  },
  productItem: {
    flex: 1,
    marginBottom: 15,
    backgroundColor: "#FFF",
    marginRight: 4,
  },
  productImageBanChay: {
    width: 170,
    height: 150,
  },
  productImage: {
    width: 168,
    height: 130,
  },
  productNameBanChay: {
    textAlign: "left",
    fontWeight: "600",
    color: "#000",
    width: 170,
    marginTop: 5,
    paddingLeft: 10,
  },
  productPriceBanChay: {
    textAlign: "left",
    fontWeight: "600",
    color: "red",
    paddingLeft: 5,
    fontSize: 16,
    flexDirection: "row",
    justifyContent: "space-between",
  },
  productName: {
    textAlign: "left",
    fontWeight: "600",
    color: "#000",
    marginTop: 5,
    paddingLeft: 10,
  },
  productPrice: {
    textAlign: "left",
    fontWeight: "600",
    color: "red",
    paddingLeft: 5,
    fontSize: 16,
    flexDirection: "row",
    justifyContent: "space-between",
  },
  anhInfo: {
    marginLeft: 10,
    marginRight: 10,
    borderRadius: 50,
  },
  tieuDe: {
    paddingHorizontal: 5,
    fontSize: 24,
    fontWeight: "600",
    marginBottom: 10,
  },
  tieuDeBanChay: {
    paddingHorizontal: 26,
    fontSize: 24,
    fontWeight: "600",
    marginBottom: 10,
  },
  addToCard: {
    height: 23,
    padding: 10,
    borderRadius: 20,
    marginTop: 10,
    paddingTop: -20,
    marginRight: 10,
    paddingBottom: -10,
    marginBottom: 10,
    color: "#fff",
    backgroundColor: "green",
  },
  flatListBanChay: {
    paddingLeft: 16,
  },
});

export default HomeScreen;
