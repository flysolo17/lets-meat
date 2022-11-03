import { useAuth } from "../auth/AuthContext";

import { useNavigate } from "react-router-dom";
import React, { useState } from "react";

import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";

import { Link } from "react-router-dom";

import Box from "@mui/material/Box";

import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";

import UCULogo from "../images/logo.png";
import Frozen from "../images/frozen-logo.png";
import Stack from "@mui/material/Stack";
import LetsMeat from "../images/letsmeat.png";
import { Divider, Paper } from "@mui/material";
function Copyright(props: any) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

interface LoginPageProps {}

const LoginPage: React.FunctionComponent<LoginPageProps> = () => {
  const { login, currentUser } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const handleSubmit = (event: any) => {
    event.preventDefault();
    login(email, password);
    navigate("/");
  };

  return (
    <div className="login-card">
      <Box
        sx={{
          width: 680,
          height: "60%",
          backgroundColor: "white",
        }}
      >
        <Container
          sx={{
            height: "20%",
            width: "100%",
            backgroundColor: "#145607",
            display: "flex",
            borderLeft: 10,
            borderColor: "#8FD256",
            alignItems: "center",
          }}
        >
          <img src={UCULogo} height={60} />
        </Container>
        <Stack
          sx={{
            width: "100%",
            height: "80%",
            backgroundColor: "white",
            alignItems: "center",
            display: "flex",
            justifyContent: "center",
          }}
          direction={"column"}
        >
          <img src={LetsMeat} width={"150px"} />
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <Typography
              sx={{
                fontFamily: "Poppins",
                fontSize: 30,
                fontWeight: 700,
                fontStyle: "bold",
              }}
            >
              Login
            </Typography>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <Container
              sx={{
                display: "flex",
                alignItems: "end",
                justifyContent: "end",
              }}
            >
              <Link to={"/signup"}>{"Forgot Password"}</Link>
            </Container>

            <Button
              type="submit"
              fullWidth
              variant="contained"
              color={"success"}
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
          </Box>
        </Stack>
      </Box>
    </div>
  );
};

export default LoginPage;
