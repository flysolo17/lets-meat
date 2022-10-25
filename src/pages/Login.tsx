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
import Paper from "@mui/material/Paper";
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
const theme = createTheme();

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
      <Stack
        direction={"row"}
        spacing={5}
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          margin: 5,
        }}
      >
        <Box sx={{ padding: 2, backgroundColor: "red" }}>
          <img src={UCULogo} />
        </Box>
        <Typography component="h1" variant="h4">
          X
        </Typography>
        <img src={Frozen} width="90" height={"100"} />
      </Stack>

      
      <ThemeProvider theme={theme}>
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Paper
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              padding: 10,
            }}
            elevation={2}
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
              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
              />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                Sign In
              </Button>
              <Grid container>
                <Grid item xs>
                  <Link to={"/signup"}>{"Forgot Password"}</Link>
                </Grid>
                <Grid item>
                  <Link to={"/signup"}>{"Don't have an account? Sign up"}</Link>
                </Grid>
              </Grid>
            </Box>
          </Paper>
          <Copyright sx={{ mt: 8, mb: 4 }} />
        </Container>
      </ThemeProvider>
    </div>
  );
};

export default LoginPage;
