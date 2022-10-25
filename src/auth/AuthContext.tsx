import React, { useContext, useState, useEffect, createContext } from "react";
import { auth } from "../config/config";
import {
  onAuthStateChanged,
  createUserWithEmailAndPassword,
  User,
  signInWithEmailAndPassword,
} from "firebase/auth";
interface IAuthContextProps {
  currentUser: User;
  signup: (email: string, password: string) => void;
  login: (email: string, password: string) => void;
  logout: any;
}
const AuthContext = createContext<IAuthContextProps>({} as IAuthContextProps);

export function useAuth() {
  return useContext(AuthContext);
}
export interface AuthProviderProps {
  children: any;
}

export const AuthProvider: React.FunctionComponent<AuthProviderProps> = (
  props
) => {
  const { children } = props;
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [loading, setLoading] = useState<boolean>(false);


  const login = (email: string, password: string) => {
    setLoading(true);
    signInWithEmailAndPassword(auth, email, password)
      .then((credential) => {
        console.log(credential.user);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
      });
  };
  const signup = (email: string, password: string) => {
    setLoading(true);
    createUserWithEmailAndPassword(auth, email, password)
      .then((credential) => {
        console.log(credential.user);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
      });
  };
  const logout = () => {
    auth
      .signOut()
      .then(() => {
        setCurrentUser(null);
      })
      .catch(() => {
        console.log("error logout");
      })
      .finally(() => {
        console.log("logout successful");
      });
  };
  const value = {
    currentUser: currentUser!,
    login,
    logout,
    signup,
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
