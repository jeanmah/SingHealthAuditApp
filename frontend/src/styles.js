import { makeStyles } from "@material-ui/core/styles";
import singhealthBackground from "./images/singhealth_building.png";

const useStyles = makeStyles((theme) => ({
  main: {
    backgroundColor: "white",
    height: "100vh",
  },
  navbar: {
    width: "100%",
    height: "13%",
    zIndex: 1,
  },
  body: {
    width: "100%",
    height: "87%",
  },
  container: {
    backgroundColor: "lightblue",
  },
  big_textfield: {
    width: "96%",
    marginLeft: "2%",
    marginRight: "2%",
  },
  buttons: {
    marginTop: "10px",
    marginRight: "10px",
  },
  big_buttons: {
    marginTop: "1%",
    marginLeft: "2%",
    marginRight: "2%",
    width: "96%",
  },
  big_bottom_buttons: {
    marginTop: "1%",
    marginLeft: "2%",
    marginRight: "2%",
    width: "96%",
    position: "absolute",
    bottom: 0,
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
    marginTop: "40%",
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
  chat_bubble: {
    border: '0.5px solid black',
    borderRadius: '10px',
    margin: '5px',
    padding: '10px',
    display: 'inline-block',
    marginLeft: '2%',
    width: '96%',
    height: '20%',
    backgroundColor: 'white',
  },
  bubbleContainer: {
    width: '100%'
  },
  leftBubble: {
    border: '0.5px solid black',
    borderRadius: '10px',
    margin: '5px',
    padding: '10px',
    display: 'inline-block',
    marginLeft: '2%',
    width: '60%',
    backgroundColor: 'white',
  },
  rightBubble: {
    border: '0.5px solid black',
    borderRadius: '10px',
    margin: '5px',
    padding: '10px',
    display: 'inline-block',
    marginLeft: '38%',
    width: '60%',
    backgroundColor: 'lightblue',
  },
  right: {
    marginLeft: 'auto',
  },
  chat_edit: {
    marginTop: "10px",
    width: '100%',
  },
  chat_entry_edit: {
    position: "absolute",
    bottom: "2%",
    width: "100%",
    height: "25%",
  },
  message_input: {
    marginTop: "7px",
    width: '96%',
    marginLeft: "2%",
  },
  chat_list: {
    position: "absolute",
    height: "60%",
    overflow: "scroll",
    width: "100%"
  },
  chat_entries_list: {
    position: "absolute",
    height: "50%",
    overflow: "scroll",
    width: "100%"
  },
  chat_messages: {
    position: "absolute",
    height: "100px",
    overflow: "auto",
  },
  annoucement_title_div: {
    position: "absolute",
    bottom: "71%",
    width: "100%",
  },
  annoucement_title: {
    marginLeft: "4%",
  },
  announcement_list: {
    backgroundColor: "lightblue",
    position: "absolute",
    bottom: "10%",
    height: "60%",
    overflow: "scroll",
    width: "100%"
  },
  announcement_bubble: {
    border: '0.5px solid black',
    borderRadius: '10px',
    margin: '5px',
    padding: '10px',
    display: 'inline-block',
    marginLeft: '2%',
    width: '96%',
    backgroundColor: 'white',
    marginBottom: "1%",
    marginTop: "1%",
  },
  search_bar: {
    width: "96%",
    position: "relative",
    left: "2%",
    top: "2%",
  },
  post_new_accouncement_div: {
    position: "absolute",
    bottom: "2%",
    width: "100%",
    height: "10%",
  },
  post_new_announcement_dialog: {
  },
  new_announcement_input: {
    marginTop: "10px",
    width: '96%',
    marginLeft: "2%",
  },
  dialog_link: {
    position: "absolute",
    left: "8%",
  },
  dialog_selector: {
    width: "96%",
    marginLeft: "2%",
    marginTop: "5%",
  },
  dialog_date_picker: {
    width: "94%",
  },
}));

export default useStyles;
