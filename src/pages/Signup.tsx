import {
  Paper,
  Stack,
  Divider,
  Typography,
  TextField,
  Box,
  Button,
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
        <Paper
          sx={{
            width: 1080,
            height: 680,
            borderRadius: 10,
            backgroundColor: "white",
            padding: 2,
          }}
          elevation={1}
        >
          <Stack sx={{ width: "100%", height: "100%" }} direction={"column"}>
            <Box
              sx={{
                padding: 3,
                backgroundColor: "red",
                margin: 2,
                width: 250,
                alignSelf: "center",
              }}
            >
              <img src={UCULogo} />
            </Box>
            <Stack direction={"row"} spacing={1} margin={2}>
              <TextField
                fullWidth
                required
                label={"Firstname"}
                value={user.firstName}
                onChange={(e) =>
                  setUser({ ...user, firstName: e.target.value })
                }
              />
              <TextField
                fullWidth
                required
                label={"Middlename"}
                value={user.middleName}
                onChange={(e) =>
                  setUser({ ...user, middleName: e.target.value })
                }
              />
              <TextField
                fullWidth
                required
                label={"Lastname"}
                value={user.lastName}
                onChange={(e) => setUser({ ...user, lastName: e.target.value })}
              />
            </Stack>
            <Stack direction={"row"} spacing={1} margin={2}>
              <TextField
                fullWidth
                required
                label={"Branchname"}
                value={user.branchName}
                onChange={(e) =>
                  setUser({ ...user, branchName: e.target.value })
                }
              />
              <TextField
                fullWidth
                required
                label={"Branch code"}
                type={"number"}
                size={"medium"}
              />
            </Stack>
            <Stack direction={"row"} spacing={1} margin={2}>
              <TextField
                fullWidth
                label={"Email"}
                type={"email"}
                value={user.email}
                required
                onChange={(e) => setUser({ ...user, email: e.target.value })}
              />
              <TextField
                fullWidth
                required
                label={"Password"}
                type={"password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Stack>
            <Button
              fullWidth={false}
              variant="contained"
              color="success"
              onClick={createAccount}
            >
              Sign up
            </Button>
          </Stack>
        </Paper>
      </div>
    </>
  );
};

export default SignupPage;
