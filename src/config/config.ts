import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";
const firebaseConfig = {
  apiKey: "AIzaSyDO0woVu3RDsq5qTkx1KvwSfsy4CLK__c8",
  authDomain: "let-s-meat-67f65.firebaseapp.com",
  projectId: "let-s-meat-67f65",
  storageBucket: "let-s-meat-67f65.appspot.com",
  messagingSenderId: "780547246274",
  appId: "1:780547246274:web:901bc19ba8378431d6135b",
};

const app = initializeApp(firebaseConfig);
export const firestore = getFirestore(app);
export const auth = getAuth(app);
