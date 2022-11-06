import React, { useState } from "react";
import {
  Box,
  Container,
  Stack,
  Typography,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Button,
} from "@mui/material";
import { Staff } from "../models/Staff";
import { deleteDoc, doc } from "firebase/firestore";
import { firestore } from "../config/config";
import { STAFF_TABLE } from "../utils/Constants";

interface StaffCardInfoProps {
  staff: Staff;
  id: string;
}

const StaffCardInfo: React.FunctionComponent<StaffCardInfoProps> = (props) => {
  const { staff, id } = props;
  const [open, setOpen] = useState(false);
  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };
  const deleteStaff = () => {
    deleteDoc(doc(firestore, STAFF_TABLE, id))
      .then(() => {
        console.log("staff Deleted!");
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        handleClose();
      });
  };
  return (
    <div>
      <Stack
        sx={{ width: "100%" }}
        direction={"column"}
        onClick={handleClickOpen}
      >
        <Stack direction={"row"} spacing={2} sx={{ padding: 2 }}>
          {staff.profile != "" ? (
            <img
              src={staff.profile}
              alt="Product"
              width={200}
              height={200}
              style={{ borderRadius: 10, backgroundColor: "#F9FFEF" }}
            />
          ) : (
            <Box
              sx={{
                backgroundColor: "#D6F7A9",
                width: 200,
                height: 200,
              }}
            ></Box>
          )}

          <Stack
            direction={"column"}
            spacing={1}
            sx={{
              width: "100%",
              height: "100%",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <Typography
              sx={{
                fontFamily: "Poppins",
                fontWeight: "700",
                fontStyle: "bold",
                fontSize: 30,
                color: "black",
              }}
            >
              {staff.fullname}
            </Typography>
            <Typography
              sx={{
                fontFamily: "Poppins",
                fontWeight: "400",
                fontStyle: "normal",
                fontSize: 24,
                color: "black",
              }}
            >
              {staff.pin}
            </Typography>
          </Stack>
        </Stack>
        <Container
          sx={{
            backgroundColor: "#145607",
            padding: 2,
            borderRadius: 1,
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <Typography
            sx={{
              fontFamily: "Poppins",
              fontWeight: "700",
              fontStyle: "bold",
              fontSize: 20,
              color: "white",
              textAlign: "Left",
            }}
          >
            Contact #: {staff.contactNumber}
          </Typography>
          <Typography
            sx={{
              fontFamily: "Poppins",
              fontWeight: "700",
              fontStyle: "bold",
              fontSize: 20,
              color: "white",
            }}
          >
            {staff.displayName}
          </Typography>
        </Container>
      </Stack>
      <Dialog
        open={open}
        onClose={handleClose}
        maxWidth={"md"}
        fullWidth
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle
          id="alert-dialog-title"
          sx={{
            backgroundColor: "#145607",
            color: "white",
            fontFamily: "Poppins",
            fontWeight: 400,
            fontStyle: "normal",
          }}
        >
          Staff Info
        </DialogTitle>
        <DialogContent>
          <Stack direction={"row"} spacing={1} padding={2}>
            <Stack direction={"column"} spacing={0} sx={{ width: "30%" }}>
              {staff.profile != "" ? (
                <img
                  src={staff.profile}
                  alt="Staff Profile"
                  width={200}
                  height={200}
                  style={{
                    borderRadius: 5,
                  }}
                />
              ) : (
                <Box
                  sx={{
                    backgroundColor: "#D6F7A9",
                    width: 200,
                    height: 200,
                    borderRadius: 5,
                  }}
                ></Box>
              )}
            </Stack>
            <Stack direction={"column"} spacing={1} sx={{ flexGrow: 1 }}>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 18,
                }}
              >
                Pin
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 24,
                  paddingLeft: 5,
                }}
              >
                {staff.pin}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 18,
                }}
              >
                Display name
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 24,
                  paddingLeft: 5,
                }}
              >
                {staff.displayName}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 18,
                }}
              >
                Fullname
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 24,
                  paddingLeft: 5,
                }}
              >
                {staff.fullname}
              </Typography>

              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 18,
                }}
              >
                Contact #
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 24,
                  paddingLeft: 5,
                }}
              >
                {staff.contactNumber}
              </Typography>
            </Stack>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button variant="contained" color="error" onClick={deleteStaff}>
            Delete Staff
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default StaffCardInfo;
