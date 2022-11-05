import React, { useState, useEffect } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import ListItemText from "@mui/material/ListItemText";
import ListItem from "@mui/material/ListItem";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import { TransitionProps } from "@mui/material/transitions";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import Stack from "@mui/material/Stack";
import {
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import Box from "@mui/material/Box";
import HorizontalRuleIcon from "@mui/icons-material/HorizontalRule";
import Grid from "@mui/material/Grid";
import BackspaceIcon from "@mui/icons-material/Backspace";
import FiberManualRecordIcon from "@mui/icons-material/FiberManualRecord";
import { useNavigate } from "react-router-dom";

interface PinViewProps {
  adminPin: string;
}

const PinView: React.FunctionComponent<PinViewProps> = (props) => {


  const { adminPin } = props;
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

  const isPinCorrect = () => {
    if (pin == adminPin) {
      navigate("/");
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
    <>
      <div>
        <Stack
          direction={"column"}
          spacing={1}
          onClick={handleClickOpen}
          sx={{
            padding: 1,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            "&:hover": {
              backgroundColor: "#D6F7A9",
              boxShadow: "none",
              borderRadius: "10px",
            },
          }}
        >
          <AdminPanelSettingsIcon sx={{ width: 50, height: 50 }} />
          <Typography
            sx={{
              fontFamily: "Poppins",
              fontSize: 20,
              fontWeight: 700,
              fontStyle: "bold",
            }}
          >
            Admin
          </Typography>
        </Stack>
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
    </>
  );
};

export default PinView;
