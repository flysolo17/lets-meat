import {
  Paper,
  Stack,
  Divider,
  Typography,
  TextField,
  Box,
  Button,
  Container,
} from "@mui/material";
import UCULogo from "../images/logo.png";
import React, { useState } from "react";
import { Users } from "../auth/models/Users";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";
interface SignupPageProps {}

const SignupPage: React.FunctionComponent<SignupPageProps> = () => {
  const { signup } = useAuth();
  const [user, setUser] = useState<Users>({
    id: "",
    firstName: "",
    middleName: "",
    lastName: "",
    branchName: "",
    branchCode: "",
    email: "",
  });
  const navigate = useNavigate();
  const [password, setPassword] = useState("");
  const createAccount = () => {
    signup(user.email, password, user);
    navigate("/");
  };
  return (
    <>
      <div className="login-card">
        <Box
          sx={{
            width: 1080,
            height: "60%",
            backgroundColor: "white",
          }}
        >
          <Container
            sx={{
              height: "20%",
              width: "100%",
              borderLeft: 10,
              borderColor: "#8FD256",
              backgroundColor: "#145607",
              display: "flex",
              alignItems: "center",
            }}
          >
            <img src={UCULogo} height={60} />
          </Container>
        </Box>
      </div>
    </>
  );
};

export default SignupPage;
