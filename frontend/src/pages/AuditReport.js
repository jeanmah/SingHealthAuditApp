import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Navbar from "../Navbar";
import { makeStyles } from "@material-ui/core/styles";
import { Context } from "../Context";
import Loading from "./Loading";

const useStyles = makeStyles((theme) => ({
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
}));

function AuditReport() {
  const { reportId } = useParams();
  const { getReportStats, getReportEntry } = useContext(Context);
  const classes = useStyles();
  const [failedEntries, setFailedEntries] = useState();

  useEffect(() => {
    async function getResponse() {
      try {
        const entryArray = await getReportStats(reportId).then((response) => {
          return response.data.Failed_Entries;
        });
        // console.log(entryArray);

        let entryInfoArray = [];

        for (let i = 0; i < entryArray.length; i++) {
          // console.log(entryArray[i]);
          // console.log(reportId);
          let info = await getReportEntry(reportId, entryArray[i]).then(
            (response) => {
              // console.log(response);
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
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default AuditReport;
