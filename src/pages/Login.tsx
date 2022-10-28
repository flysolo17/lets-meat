import { signInWithEmailAndPassword } from "firebase/auth";
import { useAuth } from "../auth/AuthContext";
import { auth } from "../config/config";
import { useNavigate } from "react-router-dom";
import React, { useRef, useState } from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import { Link } from "react-router-dom";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import UCULogo from "../images/logo.png";
import Frozen from "../images/frozen-logo.png";
import Stack from "@mui/material/Stack";

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
      <Paper
        sx={{
          width: 1080,
          height: 680,
          borderRadius: 10,
          backgroundColor: "white",
        }}
        elevation={1}
      >
        <Stack
          sx={{
            width: "100%",
            height: "100%",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
          direction={"row"}
        >
          <Container component="main" maxWidth="xs" sx={{ width: "50%" }}>
            <CssBaseline />
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
              }}
            >
              <Typography component="h1" variant="h5">
                Sign in
              </Typography>
              <Typography component="h1" variant="h6">
                Welcome Back!
              </Typography>
              <Box
                component="form"
                onSubmit={handleSubmit}
                noValidate
                sx={{ mt: 1 }}
              >
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
                  sx={{ mt: 3, mb: 2 }}
                >
                  Sign In
                </Button>

                <Container
                  sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <Link to={"/signup"}>{"Don't have an account? Sign up"}</Link>
                </Container>
              </Box>
            </Box>
          </Container>

          <Divider orientation="vertical" flexItem />
          <Box
            sx={{
              width: "50%",
              height: "100%",
              borderRadiusRight: "10px",
              backgroundColor: "#101828",
            }}
          >
            <Stack
              direction={"column"}
              spacing={5}
              sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                margin: 10,
              }}
            >
              <Box sx={{ padding: 2, backgroundColor: "red" }}>
                <img src={UCULogo} />
              </Box>
              <Typography component="h1" variant="h4">
                X
              </Typography>
              <img src={Frozen} width="100" height={"120"} />
              <Copyright sx={{ mt: 8, mb: 4 }} />
            </Stack>
          </Box>
        </Stack>
      </Paper>
    </div>
  );
};

export default LoginPage;
