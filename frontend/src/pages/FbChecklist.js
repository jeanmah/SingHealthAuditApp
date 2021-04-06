import React, { useState, useContext, useEffect, useCallback } from "react";
import { useParams } from "react-router-dom";
import Question from "../components/Question";
import { Context } from "../Context";
import Loading from "./Loading";
import Navbar from "../Navbar";
import { makeStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";

import Typography from "@material-ui/core/Typography";

//styling for the fbchecklist page
const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: theme.spacing(4, 0, 10, 0),
  },
  list: {
    width: "100%",
    maxWidth: 800,
    backgroundColor: theme.palette.background.paper,
    display: "flex",
    flexDirection: "column",

    // alignItems: "center",
  },
  button: {
    color: "#F15A22",
    fontWeight: "medium",
    width: "100%",
    maxWidth: 800,
    backgroundColor: theme.palette.background.paper,
    height: 50,
  },
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 2, 2, 2),
  },
}));

function FbChecklist() {
  //use styles from function declared above
  const classes = useStyles();
  //get tenant id from url
  const { tenantId } = useParams();

  //Context
  const {
    getFbChecklistQuestions,
    createFbReportState,
    getUserInfo,
  } = useContext(Context);

  //state to update fb checklist questions
  const [fbChecklistState, setFbChecklistState] = useState();
  const [tenantName, setTenantName] = useState();

  useEffect(() => {
    //function to retrieve questions
    async function getTenantName() {
      try {
        const tenant_name = await getUserInfo(tenantId).then((response) => {
          return response.data.store_name;
        });
        console.log(tenant_name);
        setTenantName(tenant_name);
      } catch (err) {
        console.log(err);
      }
    }
    getTenantName();
    getFbChecklistQuestions()
      .then((response) => {
        setFbChecklistState(response.data);
        createFbReportState(response.data);
      })
      .catch(() => {
        console.log("fb checklist retrieval failed");
      });
  }, []);

  return (
    <>
      {fbChecklistState && tenantName ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">{tenantName} F&B Checklist</Typography>
          </Box>

          <Grid container className={classes.root}>
            <List dense className={classes.list}>
              {fbChecklistState.map((question, index) => {
                const { fb_qn_id, requirement } = question;
                const labelId = `checkbox-list-secondary-label-${fb_qn_id}`;
                return (
                  <>
                    <Question
                      key={fb_qn_id}
                      fb_qn_id={fb_qn_id}
                      requirement={requirement}
                      labelId={labelId}
                    />
                  </>
                );
              })}
            </List>
            <Button
              className={classes.button}
              size="small"
              // color="secondary"
            >
              Submit
            </Button>
          </Grid>
        </>
      ) : (
        <Loading />
      )}
    </>
    // <div>
    //   {fbChecklistState ? (
    //     <>
    //       <Navbar />
    //       <div className="category-head">
    //         <h2>FB Checklist</h2>
    //         {fbChecklistState.map((question, index) => {
    //           const { fb_qn_id, requirement } = question;
    //           return (
    //             <Question
    //               key={index}
    //               fb_qn_id={fb_qn_id}
    //               requirement={requirement}
    //               tenantId={tenantId}
    //             />
    //           );
    //         })}
    //         <br />
    //         {/* <Link to={`/tenant/${tenantId}`}> */}
    //         <button
    //           // onClick={() => {
    //           //   updateAudit(tenantId, "FB", tenantName, "unresolved");
    //           //   resetTenantFbChecklist(tenantId);
    //           // }}
    //           onClick={() => submitFbReport(1006, fbReportState)}
    //         >
    //           Submit
    //         </button>
    //         {/* </Link> */}
    //       </div>
    //     </>
    //   ) : (
    //     <Loading />
    //   )}
    // </div>
  );
}

export default FbChecklist;
