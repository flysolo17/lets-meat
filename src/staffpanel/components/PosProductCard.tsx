import { Avatar, Stack, Typography, Paper } from "@mui/material";
import { useState } from "react";
import { Products } from "../../models/Products";
import { posBackground } from "../../utils/Constants";

interface PosProductCardProps {
  product: Products;
}

const PosProductCard: React.FunctionComponent<PosProductCardProps> = (
  props
) => {
  const { product } = props;
  return (
    <Stack
      sx={{
        width: "100%",
        height: 150,
        backgroundColor: posBackground(product.isAvailable),
        "&:hover": {
          backgroundColor: "#E4E7EC",
        },
      }}
      spacing={1}
      direction={"row"}
    >
      <Avatar
        sx={{
          width: 150,
          height: 150,
        }}
        src={product.images}
        variant={"rounded"}
      >
        No image
      </Avatar>
      <Stack
        sx={{ width: "100%", height: "100%", padding: 1 }}
        spacing={1}
        direction={"column"}
      >
        <Typography
          sx={{
            height: "70%",
            fontFamily: "Poppins",
            fontSize: 20,
            color: "black",
            fontWeight: 400,
            fontStyle: "normal",
          }}
        >
          {product.productName}
        </Typography>

        <Stack
          sx={{
            height: "30%",
            width: "100%",
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
          direction={"row"}
        >
          <Typography
            sx={{
              height: "70%",
              fontFamily: "Poppins",
              fontSize: 20,
              fontWeight: 700,
              fontStyle: "normal",
            }}
          >
            Stocks: {product.quantity}
          </Typography>
          <Typography
            sx={{
              height: "70%",
              fontFamily: "Poppins",
              fontSize: 20,
              color: "#FB6514",
              fontWeight: 700,
              fontStyle: "normal",
            }}
          >
            {product.price.toLocaleString("en-US", {
              style: "currency",
              currency: "PHP",
            })}
          </Typography>
        </Stack>
      </Stack>
    </Stack>
  );
};

export default PosProductCard;
