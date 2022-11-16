import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext";
import AuthRoute from "./components/AuthRoute";
import LandingPage from "./components/Landing";
import LoginPage from "./pages/Login";
import MainPage from "./pages/Main";
import SignupPage from "./pages/Signup";
import "./App.css";
import NavBar from "./components/NavBar";

import DashBoard from "./adminpanel/Dashboard";

import AccountPage from "./adminpanel/Account";
import InventoryPage from "./adminpanel/Inventory";
import DashBoardPage from "./adminpanel/Dashboard";
import StaffPage from "./adminpanel/Staff";
import StaffMainPage from "./staffpanel/StaffMain";
import StaffNavBar from "./components/StaffNavBar";
import StaffCashDrawerPage from "./staffpanel/StaffCashDrawer";
import StaffAccountPage from "./staffpanel/StaffAccount";
interface ApplicationProps {}

const Application: React.FunctionComponent<ApplicationProps> = (props) => {
  return (
    <>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route
              path={"/main"}
              element={
                <AuthRoute>
                  <MainPage />
                </AuthRoute>
              }
            />

            <Route path={"/cashier"}>
              <Route
                index
                element={
                  <StaffNavBar>
                    <StaffMainPage />
                  </StaffNavBar>
                }
              />
              <Route
                path="drawer"
                element={
                  <StaffNavBar>
                    <StaffCashDrawerPage />
                  </StaffNavBar>
                }
              />
              <Route
                path="home"
                element={
                  <StaffNavBar>
                    <StaffMainPage />
                  </StaffNavBar>
                }
              />
              <Route
                path="account"
                element={
                  <StaffNavBar>
                    <StaffAccountPage />
                  </StaffNavBar>
                }
              />
            </Route>
            <Route
              path={"/"}
              element={
                <AuthRoute>
                  <NavBar>
                    <DashBoardPage />
                  </NavBar>
                </AuthRoute>
              }
            />
            <Route
              path={"/inventory"}
              element={
                <AuthRoute>
                  <NavBar>
                    <InventoryPage />
                  </NavBar>
                </AuthRoute>
              }
            />
            <Route
              path={"/staff"}
              element={
                <AuthRoute>
                  <NavBar>
                    <StaffPage />
                  </NavBar>
                </AuthRoute>
              }
            />

            <Route
              path={"/account"}
              element={
                <AuthRoute>
                  <NavBar>
                    <AccountPage />
                  </NavBar>
                </AuthRoute>
              }
            />
            <Route
              path={"/dashboard"}
              element={
                <AuthRoute>
                  <NavBar>
                    <DashBoardPage />
                  </NavBar>
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
