import React, { useState, useEffect, useContext } from "react";
import Navbar from "../Navbar";
import emailjs from "emailjs-com";
import Box from "@material-ui/core/Box";
import { useParams } from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import { Context } from "../Context";
import Loading from "./Loading";

const useStyles = makeStyles((theme) => ({
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
  emailFields: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: theme.spacing(2, 0, 0, 0),
  },
  field: {
    maxWidth: 800,
    width: "80%",
    margin: theme.spacing(2, 0, 0, 0),
  },
  buttonSubmit: {
    margin: theme.spacing(8, 0, 0, 0),
    width: 240,
    // color: "#F15A22",
    fontWeight: "medium",
    backgroundColor: "#F15A22",
  },
}));

function AuditEmail() {
  const classes = useStyles();
  const { reportId } = useParams();
  const [toEmail, setToEmail] = useState("");
  const [checklistReport, setChecklistReport] = useState("");
  const [toName, setToName] = useState("");
  const [storeName, setStoreName] = useState("");
  const [checklistType, setChecklistType] = useState();
  const [dateOfAudit, setDateOfAudit] = useState("");
  const [score, setScore] = useState("");

  const { getReport, getQuestionInfo, getOriginalReport } = useContext(Context);

  //entry fields needed:
  //1. qn_id
  //2. images
  //  3. remarks
  //  4. severity / timeframe
  //  5. status
  // 6. requirement

  useEffect(() => {
    async function emailAsync() {
      try {
        const reportInfo = await getReport(reportId).then((response) => {
          console.log(response);
          return response.data;
        });

        console.log(
          await getQuestionInfo(reportId, reportInfo.entries[0].qn_id).then(
            (response) => {
              return response;
            }
          )
        );
        console.log(
          await getOriginalReport(reportId).then((response) => {
            return response;
          })
        );

        let checklistString = "";
        let count = 1;
        for (let i = 0; i < 96; i++) {
          let severity = await getQuestionInfo(reportId, i + 1).then(
            (response) => {
              return response.data.severity;
            }
          );
          severity /= 1000000;
          severity = Math.floor(severity);
          let timeframe = "";
          switch (severity) {
            case 1:
              timeframe = "3 weeks";
              break;
            case 2:
              timeframe = "2 weeks";
              break;
            case 3:
              timeframe = "1 week";
              break;
            case 4:
              timeframe = "4 days";
              break;
            case 5:
              timeframe = "1 day";
              break;
          }
          checklistString += `Question ${i + 1}: ${await getQuestionInfo(
            reportId,
            i + 1
          ).then((response) => {
            return response.data.requirement;
          })} \nRemarks: ${await getQuestionInfo(reportId, i + 1).then(
            (response) => {
              return response.data.original_remarks;
            }
          )} \nRectification Period: ${timeframe}\n`;
          count++;
        }
        console.log(checklistString);
        console.log(count);
        if (count === 97) {
          setChecklistReport(checklistString);
        }
        setStoreName(reportInfo.store_name);
        setChecklistType(reportInfo.report_type);
        setDateOfAudit(new Date(reportInfo.open_date).toString());
        setScore(reportInfo.overall_score);
      } catch (err) {
        console.log(err);
      }
    }

    emailAsync();
  }, []);

  //emailjs function
  function sendEmail(e) {
    e.preventDefault();

    emailjs
      .sendForm(
        "service_1xo642c",
        "checklist_template",
        e.target,
        "user_Y6aBIfzMOeWunufHbkvwx"
      )
      .then(
        (result) => {
          console.log(result.text);
        },
        (error) => {
          console.log(error.text);
        }
      );
  }
  const handleToEmail = (e) => {
    setToEmail(e.target.value);
  };

  const handleToName = (e) => {
    setToName(e.target.value);
  };

  return (
    <div>
      {checklistReport ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">Set Email Fields</Typography>
          </Box>
          <form className={classes.emailFields} onSubmit={sendEmail}>
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Key in recipient email address"
              name="to_email"
              autoFocus={true}
              required={true}
              onChange={(e) => {
                handleToEmail(e);
              }}
              value={toEmail}
            />
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Key in name of recipient"
              name="to_name"
              required={true}
              onChange={(e) => {
                handleToName(e);
              }}
              value={toName}
            />
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Store Name"
              name="store_name"
              value={storeName}
            />
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Checklist Type"
              name="checklist_type"
              value={checklistType}
            />
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Date of Conducted Audit"
              name="date"
              value={dateOfAudit}
            />
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Audit Score"
              name="score"
              value={score}
            />
            <TextField
              className={classes.field}
              id="standard-basic"
              label="Checklist"
              name="checklist"
              value={checklistReport}
            />
            {/* <input type="submit" value="Send"> */}
            <Button
              type="submit"
              className={classes.buttonSubmit}
              variant="contained"
              color="primary"
              size="large"
              // onClick={() => {
              //   sendEmail();
              // }}
              color="secondary"
            >
              send email
            </Button>
            {/* </input> */}
          </form>{" "}
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default AuditEmail;
