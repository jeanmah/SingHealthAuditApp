import React, { useContext, useState } from "react";
import TextField from "@material-ui/core/TextField";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import Checkbox from "@material-ui/core/Checkbox";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import { FaRegEdit } from "react-icons/fa";
import { Context } from "../Context";
import { makeStyles } from "@material-ui/core/styles";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import SentimentVeryDissatisfiedIcon from "@material-ui/icons/SentimentVeryDissatisfied";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import PropTypes from "prop-types";
import Rating from "@material-ui/lab/Rating";
import { withStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  dropdownContainer: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  dropdownSection: {
    display: "flex",
    justifyContent: "space-around",
    width: "100%",
    // justifyContent: "center",
  },
  commentBox: {
    // margin: theme.spacing(0, 2, 0, 2),
    width: "70%",
  },
  button: {
    color: "#F15A22",
    fontWeight: "medium",
  },
}));
const StyledRating = withStyles({
  iconFilled: {
    color: "#ff6d75",
  },
  iconHover: {
    color: "#ff3d47",
  },
})(Rating);

const customIcons = {
  1: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Very Dissatisfied",
  },
  2: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Dissatisfied",
  },
  3: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Neutral",
  },
  4: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Satisfied",
  },
  5: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Very Satisfied",
  },
};

function IconContainer(props) {
  const { value, ...other } = props;
  return <span {...other}>{customIcons[value].icon}</span>;
}

IconContainer.propTypes = {
  value: PropTypes.number.isRequired,
};

function Question({ fb_qn_id, requirement, labelId }) {
  const classes = useStyles();

  const { fbReportState, setFbReportState } = useContext(Context);

  //state to update number of checked boxes
  const [checked, setChecked] = useState([]);
  //state to update comments
  const [comment, setComment] = useState("");
  //state to update severity
  const [severity, setSeverity] = useState(0);

  //handle the checkbox changes
  const handleToggle = (question_id) => () => {
    // current question id
    const currentIndex = checked.indexOf(question_id);

    const newChecked = [...checked];

    if (currentIndex === -1) {
      newChecked.push(question_id);
    } else {
      newChecked.splice(currentIndex, 1);
    }
    // update the checked state
    setChecked(newChecked);
    console.log(fbReportState);
    //update fb report state
    setFbReportState((prevState) => {
      return prevState.map((question) =>
        question.qn_id === question_id
          ? { ...question, status: !question.status }
          : question
      );
    });
  };
  //function to update comment state
  const handleComment = (e) => {
    setComment(e.target.value);
  };
  //function to update severity state
  const handleSeverity = (e) => {
    setSeverity(e.target.value);
    // console.log(e.target.value);
  };
  //function to update fb report state upon clicking save
  const handleSave = () => {
    let today = new Date();
    // console.log(severity);

    switch (severity) {
      case "1":
        today.setDate(today.getDate() + 21);
        break;
      case "2":
        today.setDate(today.getDate() + 14);
        break;
      case "3":
        today.setDate(today.getDate() + 7);
        break;
      case "4":
        today.setDate(today.getDate() + 4);
        break;
      case "5":
        today.setDate(today.getDate() + 1);
        break;
      default:
        today.setDate(today.getDate());
        break;
    }
    // console.log(today);

    let severityDate =
      (today.getDate() < 10
        ? "0" + today.getDate().toString()
        : today.getDate().toString()) +
      (today.getMonth() < 10
        ? "0" + today.getMonth().toString()
        : today.getMonth().toString()) +
      today.getFullYear().toString().slice(2, 4);
    console.log(severityDate);
    setFbReportState((prevState) => {
      return prevState.map((question) =>
        question.qn_id === fb_qn_id && severity !== "0"
          ? {
              ...question,
              severity: parseInt(severity + severityDate),
              remarks: comment,
            }
          : question
      );
    });
  };

  return (
    <div>
      <Accordion>
        <AccordionSummary
          key={fb_qn_id}
          expandIcon={<ExpandMoreIcon />}
          aria-label="Expand"
          aria-controls="additional-actions1-content"
          id="additional-actions1-header"
        >
          <ListItem button>
            <ListItemText id={fb_qn_id} primary={`${requirement}`} />
            <Checkbox
              // edge="end"
              onChange={handleToggle(fb_qn_id)}
              checked={checked.indexOf(fb_qn_id) === -1}
              inputProps={{ "aria-labelledby": labelId }}
              className={classes.checkbox}
            />
          </ListItem>
        </AccordionSummary>
        <AccordionDetails className={classes.dropdownContainer}>
          <div className={classes.dropdownSection}>
            <TextField
              id="standard-multiline-static"
              label="Comment on non-compliance"
              multiline
              value={comment}
              onChange={(e) => {
                handleComment(e);
              }}
              className={classes.commentBox}
            />
            <Box component="fieldset" mb={3} borderColor="transparent">
              <Typography component="legend">Set Severity</Typography>
              <Rating
                name={`${fb_qn_id}`}
                defaultValue={0}
                // getLabelText={(value) => customIcons[value].label}
                IconContainerComponent={IconContainer}
                onChange={(e) => {
                  handleSeverity(e);
                }}
              />
            </Box>
          </div>
          <div>
            <Button
              className={classes.button}
              size="small"
              onClick={() => {
                handleSave();
              }}
              // color="secondary"
            >
              save
            </Button>
          </div>
        </AccordionDetails>
      </Accordion>
    </div>
  );
}

export default Question;
