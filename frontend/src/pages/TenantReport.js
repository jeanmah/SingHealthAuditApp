import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Navbar from "../Navbar";
import { makeStyles } from "@material-ui/core/styles";
import { Context } from "../Context";
import Loading from "./Loading";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Avatar from "@material-ui/core/Avatar";
import TenantReportCard from "../components/TenantReportCard";
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
  root: {
    width: "100%",
    padding: theme.spacing(4, 0, 10, 0),
  },
}));

function TenantReport() {
  const { reportId } = useParams();
  const {
    getReportStats,
    getReportEntry,
    getFbChecklistQuestions,
    createFbReportState,
  } = useContext(Context);
  const classes = useStyles();
  const [failedEntries, setFailedEntries] = useState();

  useEffect(() => {
    async function getResponse() {
      try {
        // getFbChecklistQuestions().then((response) => {
        //   createFbReportState(response.data);
        // });

        const entryArray = await getReportStats(reportId).then((response) => {
          console.log(response);
          return response.data.Failed_Entries;
        });
        // console.log(entryArray);

        let entryInfoArray = [];

        for (let i = 0; i < entryArray.length; i++) {
          // console.log(entryArray[i]);
          // console.log(reportId);
          let info = await getReportEntry(reportId, entryArray[i]).then(
            (response) => {
              console.log(response);
              return response;
            }
          );
          entryInfoArray.push(info);
        }
        if (entryInfoArray.length === entryArray.length) {
          console.log(entryInfoArray);
          setFailedEntries(entryInfoArray);
        }
      } catch (err) {
        console.error(err);
      }
    }
    getResponse();
  }, []);

  return (
    <div>
      {failedEntries ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">Report Non-Compliance Overview</Typography>
          </Box>
          <Box className={classes.reportBasicInfo}></Box>
          <div className={classes.root}>
            {failedEntries.map((entry) => {
              const { data } = entry;
              let severity = data.severity / 1000000;
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
              return (
                <TenantReportCard
                  remarks={data.remarks}
                  entry_id={data.entry_id}
                  requirement={data.Requirement}
                  timeframe={timeframe}
                  report_id={reportId}
                />
              );
            })}
          </div>
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default TenantReport;
