import React, { useState, useEffect, useContext } from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Context } from "../Context";
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
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
  title: {
    display: "flex",
    flexDirection: "row",
  },
  titleResolved: {
    padding: theme.spacing(0, 2, 0, 2),
    color: "#F15A22",
  },

  dropdownMain: {
    display: "flex",
    flexDirection: "column",
  },
  dropdownContainer: {
    display: "flex",
    flexDirection: "column",
    margin: theme.spacing(0, 0, 0, 2),
  },
  textInfo: {
    padding: theme.spacing(2, 0, 2, 0),
    color: "#F15A22",
  },
  topText: {
    color: "#F15A22",
  },
  textTenant: {
    padding: theme.spacing(1, 0, 2, 2),
    fontWeight: "medium",
  },
  tenantResponse: {
    display: "flex",
    padding: theme.spacing(2, 0, 2, 2),
    backgroundColor: theme.palette.background.default,
  },
  tenantTextResponse: {
    // margin: theme.spacing(0, 2, 5, 2),
    padding: theme.spacing(0, 0, 0, 2),
    backgroundColor: theme.palette.background.default,
  },
  tenantResponseContainer: {
    margin: theme.spacing(3, 7, 3, 2),
    padding: theme.spacing(2, 2, 2, 2),
    backgroundColor: theme.palette.background.default,
    display: "flex",
    flexDirection: "column",
    maxWidth: 700,
  },
  avatar: {},
  image: {
    width: "100%",
    maxWidth: 400,
    padding: theme.spacing(3, 2, 2, 2),
  },
  button: {
    width: 240,
    // color: "#F15A22",
    fontWeight: "medium",
    backgroundColor: "#F15A22",
  },
  buttonContainer: {
    padding: theme.spacing(3, 2, 3, 2),
    display: "flex",

    // justifyContent: "center",
  },
  // media: {
  //   width: 100,
  //   height: 100,
  //   backgroundColor: theme.palette.background.default,
  // },
}));

function AuditReportCard({
  current_qn_status,
  requirement,
  original_remarks,
  timeframe,
  report_id,
  tenant_id,
  qn_id,
}) {
  const classes = useStyles();

  //state of tenant rectification
  // const [tenantRemarks, setTenantRemarks] = useState();
  // const [tenantRectificationImage, setTenantRectificationImage] = useState();
  // const [failedEntries, setFailedEntries] = useState();
  const [tenantResponse, setTenantResponse] = useState();

  const { getTenantRectification, submitReportUpdate } = useContext(Context);

  useEffect(() => {
    async function getResponse() {
      try {
        //GET TENANT RECTIFICATION RESPONSES
        getTenantRectification(report_id, tenant_id, qn_id)
          .then((response) => {
            console.log(response.data.entries);

            setTenantResponse(response.data.entries);
          })
          .catch((error) => {
            console.log(error);
          });
        // console.log(tenantEntry);
      } catch (error) {
        console.log(error);
      }
    }
    getResponse();
  }, []);

  const resolveNonCompliance = () => {
    async function resolveAsync() {
      // const entry = await getReportEntry(report_id, qn_id).then((response) => {
      //   console.log(response.data);
      //   return response.data;
      // });
      submitReportUpdate(report_id, false, "", {
        qn_id: qn_id,
        status: true,
      });
    }
    resolveAsync();
  };

  return (
    <>
      {tenantResponse && (
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-label="Expand"
            aria-controls="additional-actions1-content"
            id="additional-actions1-header"
          >
            <ListItem className={classes.title}>
              <ListItemText id={qn_id} primary={requirement} />
              {current_qn_status === "PASS" && (
                <ListItemText className={classes.titleResolved}>
                  <Typography variant="button">Resolved</Typography>
                </ListItemText>
              )}
            </ListItem>
          </AccordionSummary>
          <AccordionDetails className={classes.dropdownMain}>
            <div className={classes.dropdownContainer}>
              <Typography
                color="textSecondary"
                variant="button"
                className={classes.topText}
              >
                YOUR REMARKS: {original_remarks}
              </Typography>

              <Typography
                color="textSecondary"
                variant="button"
                className={classes.textInfo}
              >
                RECTIFICATION PERIOD: {timeframe}
              </Typography>
            </div>
            {tenantResponse.map((response) => {
              const { remarks, images } = response;

              return (
                <>
                  <Box
                    className={classes.tenantResponseContainer}
                    boxShadow={2}
                  >
                    <div className={classes.tenantResponse}>
                      <Avatar
                        src="/broken-image.jpg"
                        className={classes.avatar}
                      />
                      <Typography
                        color="textPrimary"
                        className={classes.textTenant}
                      >
                        Tenant Response:
                      </Typography>
                    </div>

                    <Typography
                      color="textPrimary"
                      // variant="h8"
                      className={classes.tenantTextResponse}
                      variant="caption"
                    >
                      {remarks}
                    </Typography>

                    {images.length !== 0 && (
                      <img src={images[0]} className={classes.image}></img>
                    )}
                  </Box>
                  {current_qn_status === "FAIL" && (
                    <div className={classes.buttonContainer}>
                      <Button
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        size="large"
                        onClick={() => {
                          resolveNonCompliance();
                        }}
                        // color="secondary"
                      >
                        resolve
                      </Button>
                    </div>
                  )}
                </>
              );
            })}
          </AccordionDetails>
        </Accordion>
      )}{" "}
    </>
  );
}

export default AuditReportCard;
