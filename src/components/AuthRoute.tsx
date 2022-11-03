import { auth } from "../config/config";
import React, { useEffect } from "react";
import { onAuthStateChanged } from "firebase/auth";
import { useNavigate } from "react-router-dom";

export interface IAuthRouteProps {
  children: any;
}
const AuthRoute: React.FunctionComponent<IAuthRouteProps> = (props) => {
  const { children } = props;

  const navigate = useNavigate();

  useEffect(() => {
    const unsub = onAuthStateChanged(auth, (currentUser) => {
      if (currentUser != null) {
        navigate("/main");
      } else {
        navigate("/login");
      }
    });
    return () => unsub();
  }, []);

  return <>{children}</>;
};

export default AuthRoute;
