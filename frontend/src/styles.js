import { makeStyles } from "@material-ui/core/styles";
import singhealthBackground from "./images/singhealth_building.png";

const useStyles = makeStyles((theme) => ({
  main: {
    backgroundColor: "white",
  },
  container: {
    backgroundColor: "lightblue",
  },
  buttons: {
    marginTop: "10px",
  },
  root: {
    height: "100vh",
  },
  image: {
    backgroundImage: `url(${singhealthBackground})`,
    backgroundRepeat: "no-repeat",
    backgroundColor:
      theme.palette.type === "light"
        ? theme.palette.grey[50]
        : theme.palette.grey[900],
    backgroundSize: "cover",
    backgroundPosition: "center",
  },
  paper: {
    margin: theme.spacing(8, 4),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: "100%", // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default useStyles;