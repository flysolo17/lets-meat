import { Products } from "../models/Products";
import { Box, Stack, Typography } from "@mui/material";
import Logo from "../images/letsmeat.png";
interface ProductCardProps {
  product: Products;
}

const ProductCard: React.FunctionComponent<ProductCardProps> = (props) => {
  const { product } = props;
  const getImage = (): string => {
    return product.images != "" ? product.images : Logo;
  };
  return (
    <>
      <Box sx={{ height: 200, backgroundColor: "#E6FBC7", borderRadius: 5 }}>
        <Stack direction={"row"}>
          <img
            src={getImage()}
            width={200}
            height={200}
            loading={"lazy"}
            style={{ backgroundColor: "#B4E87B", borderRadius: 10 }}
          />
          <Stack direction={"column"} sx={{ paddingX: 1 }}>
            <Typography
              sx={{
                fontFamily: "Poppins",
                fontSize: 20,
                fontWeight: 500,
                overflow: "hidden",

                maxHeight: 100,
                fontStyle: "normal",
                color: "black",
              }}
            >
              {product.productName}
            </Typography>
            <Typography
              sx={{
                fontFamily: "Poppins",
                fontSize: 18,
                fontWeight: 300,

                fontStyle: "normal",
                color: "black",
              }}
            >
              {"Quantity"}
            </Typography>

            <Typography
              sx={{
                fontFamily: "Poppins",
                fontSize: 18,
                fontWeight: 400,
                fontStyle: "normal",
                color: "black",
              }}
            >
              {product.quantity}
            </Typography>
            <Stack
              direction={"row"}
              sx={{
                display: "flex",
                flexDirection: "row",
                alignItems: "center",
                width: "100%",
                justifyContent: "space-between",
              }}
            >
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontSize: 18,
                  fontWeight: 300,

                  fontStyle: "normal",
                  color: "black",
                }}
              >
                {"Cost"}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontSize: 18,
                  fontWeight: 300,

                  fontStyle: "normal",
                  color: "black",
                }}
              >
                {"Price"}
              </Typography>
            </Stack>

            <Stack
              direction={"row"}
              sx={{
                display: "flex",
                flexDirection: "row",
                alignItems: "center",
                width: "100%",
                justifyContent: "space-between",
              }}
            >
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontSize: 18,
                  fontWeight: 400,

                  fontStyle: "normal",
                  color: "black",
                }}
              >
                {product.cost}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontSize: 18,
                  fontWeight: 400,

                  fontStyle: "normal",
                  color: "black",
                }}
              >
                {product.price}
              </Typography>
            </Stack>
          </Stack>
        </Stack>
      </Box>
    </>
  );
};

export default ProductCard;
