import { initializeApp } from "firebase/app";
import { getStorage, ref, getDownloadURL } from 'firebase/storage';
// import { getStorage, ref, getDownloadURL } from '@react-native-firebase/storage';
// Cấu hình Firebase
const firebaseConfig = {
  apiKey: "YOUR-APi-KEY",
  authDomain: "fivefood-datn-8a1cf.firebaseapp.com",
  projectId: "fivefood-datn-8a1cf",
  storageBucket: "fivefood-datn-8a1cf.appspot.com",
  messagingSenderId: "690125592350",
  appId: "G-K1R5H8JS85"
};

// Khởi tạo Firebase app
const app = initializeApp(firebaseConfig);

// Lấy đối tượng Firebase Storage
const storage = getStorage(app);

// Hàm lấy URL tải xuống ảnh từ Firebase Storage
const getImageURL = async (imagePath) => {
  try {
    // Tạo tham chiếu tới file trong Firebase Storage
    const reference = ref(storage, imagePath);
    // Lấy URL của file từ Firebase Storage
    const downloadURL = await getDownloadURL(reference);
    return downloadURL; // Trả về URL của ảnh
  } catch (error) {
    console.error("Error getting image URL:", error);
  }
};

export { storage, getImageURL };
