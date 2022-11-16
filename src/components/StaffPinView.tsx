import { experimentalStyled as styled } from "@mui/material/styles";
import {
  Container,
  Box,
  Grid,
  Typography,
  Avatar,
  Stack,
  Dialog,
} from "@mui/material";
import LockIcon from "@mui/icons-material/Lock";
import { Staff } from "../models/Staff";
import React, { useState, useEffect } from "react";
import FiberManualRecordIcon from "@mui/icons-material/FiberManualRecord";
import { useNavigate } from "react-router-dom";
import HorizontalRuleIcon from "@mui/icons-material/HorizontalRule";

import BackspaceIcon from "@mui/icons-material/Backspace";

import { DialogContent, DialogContentText, DialogTitle } from "@mui/material";
import { STAFF_ID } from "../utils/Constants";

const Item = styled(Box)(({ theme }) => ({
  backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
  ...theme.typography.body2,
  padding: theme.spacing(2),
  textAlign: "center",
  fontFamily: "Poppins",
  fontStyle: "normal",
  fontWeight: 500,
  fontSize: 30,
  color: theme.palette.text.secondary,
  "&:hover": {
    backgroundColor: "#F2F4F7",
    boxShadow: "none",
    borderRadius: "10px",
  },
}));
interface StaffPinViewProps {
  row: Staff;
  id: string;
}

const StaffPinView: React.FunctionComponent<StaffPinViewProps> = (props) => {
  const { row, id } = props;

  const [open, setOpen] = useState(false);
  const [pin, setPin] = useState("");
  const [wrongPassword, setWrongPassword] = useState<String | null>(null);
  const navigate = useNavigate();
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };
  useEffect(() => {
    if (pin != "") {
      setWrongPassword(null);
    }
  }, [pin]);

  const isPinCorrect = () => {
    if (pin == row.pin) {
      localStorage.setItem(STAFF_ID, id);
      navigate("/cashier");
    } else {
      setPin(pin.substring(0, pin.length - pin.length));
      setWrongPassword("Wrong Password try again!");
    }
  };
  function FirstRow() {
    return (
      <React.Fragment>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("1"));
          }}
        >
          <Item>1</Item>
        </Grid>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("2"));
          }}
        >
          <Item>2</Item>
        </Grid>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("3"));
          }}
        >
          <Item>3</Item>
        </Grid>
      </React.Fragment>
    );
  }
  function SecondRow() {
    return (
      <React.Fragment>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("4"));
          }}
        >
          <Item>4</Item>
        </Grid>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("5"));
          }}
        >
          <Item>5</Item>
        </Grid>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("6"));
          }}
        >
          <Item>6</Item>
        </Grid>
      </React.Fragment>
    );
  }
  function ThirdRow() {
    return (
      <React.Fragment>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("7"));
          }}
        >
          <Item>7</Item>
        </Grid>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("8"));
          }}
        >
          <Item>8</Item>
        </Grid>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("9"));
          }}
        >
          <Item>9</Item>
        </Grid>
      </React.Fragment>
    );
  }
  function LastRow() {
    return (
      <React.Fragment>
        <Grid
          item
          xs={4}
          onClick={() => {
            pin.length < 4 && setPin((prev) => prev.concat("0"));
          }}
        >
          <Item>0</Item>
        </Grid>
        <Grid
          item
          xs={8}
          onClick={() => setPin(pin.substring(0, pin.length - 1))}
        >
          <Item>
            <BackspaceIcon />
          </Item>
        </Grid>
      </React.Fragment>
    );
  }
  function ActionsRow() {
    return (
      <React.Fragment>
        <Grid item xs={6} onClick={handleClose}>
          <Item>Back</Item>
        </Grid>
        <Grid item xs={6} onClick={isPinCorrect}>
          <Item>Continue</Item>
        </Grid>
      </React.Fragment>
    );
  }
  function identify(key: number, size: number) {
    if (key <= size) {
      return <FiberManualRecordIcon />;
    } else {
      return <HorizontalRuleIcon />;
    }
  }
  return (
    <div>
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          flexDirection: "column",
        }}
        onClick={handleClickOpen}
      >
        <Avatar
          src={row.profile}
          variant="rounded"
          sx={{ width: 100, height: 100 }}
        />
        <Typography
          component={"h1"}
          variant={"h6"}
          sx={{ textAlign: "center" }}
        >
          {row.displayName}
        </Typography>
        <LockIcon />
      </Box>
      <Dialog fullWidth maxWidth={"lg"} open={open} onClose={handleClose}>
        <DialogTitle
          textAlign={"center"}
          sx={{
            fontFamily: "Poppins",
            fontSize: 30,
            fontWeight: 500,
            fontStyle: "normal",
          }}
        >
          Enter Your Pin
        </DialogTitle>
        <DialogContent>
          <Stack
            direction={"column"}
            spacing={2}
            sx={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <DialogContentText
              textAlign={"center"}
              sx={{
                fontFamily: "Poppins",
                fontSize: 24,
                fontWeight: 300,
                color: "black",
                fontStyle: "normal",
              }}
            >
              {wrongPassword != null
                ? wrongPassword
                : "Do not share your pin to everyone"}
            </DialogContentText>

            <Stack direction={"row"} spacing={2}>
              {[1, 2, 3, 4].map((value) => identify(value, pin.length))}
            </Stack>
            <Box sx={{ flexGrow: 1 }}>
              <Grid container spacing={1}>
                <Grid container item spacing={3}>
                  <FirstRow />
                </Grid>
                <Grid container item spacing={3}>
                  <SecondRow />
                </Grid>
                <Grid container item spacing={3}>
                  <ThirdRow />
                </Grid>
                <Grid container item spacing={3}>
                  <LastRow />
                </Grid>
                <Grid container item spacing={3}>
                  <ActionsRow />
                </Grid>
              </Grid>
            </Box>
          </Stack>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default StaffPinView;
