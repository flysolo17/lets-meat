import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext";
import AuthRoute from "./components/AuthRoute";
import LandingPage from "./components/Landing";
import LoginPage from "./pages/Login";
import MainPage from "./pages/Main";
import SignupPage from "./pages/Signup";
import "./App.css";
import NavBar from "./components/NavBar";
interface ApplicationProps {}

const Application: React.FunctionComponent<ApplicationProps> = (props) => {
  return (
    <>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route
              path={"/"}
              element={
                <AuthRoute>
                    <MainPage />
                </AuthRoute>
              }
            />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </>
  );
};

export default Application;
