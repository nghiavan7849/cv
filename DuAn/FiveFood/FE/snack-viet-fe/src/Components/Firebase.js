import { initializeApp } from "firebase/app";
import { getStorage, ref, getDownloadURL } from "firebase/storage";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "YOUR-API",
  authDomain: "fivefood-datn-8a1cf.firebaseapp.com",
  projectId: "fivefood-datn-8a1cf",
  storageBucket: "fivefood-datn-8a1cf.appspot.com",
  messagingSenderId: "******",
  appId: "G-*****"
};

const app = initializeApp(firebaseConfig);
const storage = getStorage(app);

const db = getFirestore(app);

export { storage,db };
