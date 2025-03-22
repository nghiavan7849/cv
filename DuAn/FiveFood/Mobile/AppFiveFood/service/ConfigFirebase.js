import { initializeApp } from "firebase/app"; // Thư viện Firebase
import { getStorage } from "firebase/storage"; // Firebase Storage
import { getFirestore } from "firebase/firestore"; // Firestore

const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "fivefood-datn-8a1cf.firebaseapp.com",
  projectId: "fivefood-datn-8a1cf",
  storageBucket: "fivefood-datn-8a1cf.appspot.com",
  messagingSenderId: "...",
  appId: "..."
};

// Khởi tạo ứng dụng Firebase
const app = initializeApp(firebaseConfig);

// Khởi tạo Storage và Firestore
const storage = getStorage(app);
const db = getFirestore(app);

// Xuất Storage và Firestore để sử dụng ở nơi khác
export { storage, db };