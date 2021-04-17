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
    margin: theme.spacing(2, 0, 3, 2),
    padding: theme.spacing(2, 2, 2, 2),
    backgroundColor: theme.palette.background.default,
  },
  avatar: {},
  image: {
    width: "70%",
    padding: theme.spacing(3, 2, 2, 2),
  },
  button: {
    width: 240,
    // color: "#F15A22",
    fontWeight: "medium",
    backgroundColor: "#F15A22",
  },
  buttonContainer: {
    padding: theme.spacing(2, 2, 2, 2),
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
  entry_id,
  requirement,
  remarks,
  timeframe,
  report_id,
  tenant_id,
  qn_id,
}) {
  const classes = useStyles();
  const [comment, setComment] = useState("");
  //selected file state
  const [selectedFile, setSelectedFile] = useState();
  //state to check if file is selected
  const [isFilePicked, setIsFilePicked] = useState(false);
  //state of tenant rectification
  const [tenantRemarks, setTenantRemarks] = useState();
  const [tenantRectificationImage, setTenantRectificationImage] = useState();

  const {
    getTenantRectification,
    getReportEntry,
    submitReportUpdate,
  } = useContext(Context);

  useEffect(() => {
    async function getResponse() {
      try {
        const tenantEntry = getTenantRectification(report_id, tenant_id, qn_id)
          .then((response) => {
            console.log(response);
            setTenantRemarks(response.data.entries[0].remarks);
            if (response.data.entries[0].images.length === 1) {
              setTenantRectificationImage(response.data.entries[0].images[0]);
            } else {
              setTenantRectificationImage("No image");
            }
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

  //function to update comment state
  const handleComment = (e) => {
    setComment(e.target.value);
    // console.log(comment);
  };

  // handle input file change
  const handleChange = (event) => {
    console.log(event.target.files[0]);
    setSelectedFile(event.target.files[0]);

    setIsFilePicked(true);
  };

  const resolveNonCompliance = () => {
    async function resolveAsync() {
      const entry = await getReportEntry(report_id, entry_id).then(
        (response) => {
          console.log(response.data);
          return response.data;
        }
      );
      submitReportUpdate(report_id, false, "", {
        ...entry,
        status: true,
      });
    }
    resolveAsync();
  };

  return (
    <>
      {tenantRectificationImage && (
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-label="Expand"
            aria-controls="additional-actions1-content"
            id="additional-actions1-header"
          >
            <ListItem>
              <ListItemText id={entry_id} primary={requirement} />
            </ListItem>
          </AccordionSummary>
          <AccordionDetails className={classes.dropdownMain}>
            <div className={classes.dropdownContainer}>
              <Typography
                color="textSecondary"
                variant="button"
                className={classes.topText}
              >
                YOUR REMARKS: {remarks}
              </Typography>

              <Typography
                color="textSecondary"
                variant="button"
                className={classes.textInfo}
              >
                RECTIFICATION PERIOD: {timeframe}
              </Typography>
            </div>
            <Box className={classes.tenantResponseContainer} boxShadow={2}>
              <div className={classes.tenantResponse}>
                <Avatar src="/broken-image.jpg" className={classes.avatar} />
                <Typography color="textPrimary" className={classes.textTenant}>
                  Tenant Response:
                </Typography>
              </div>
              {tenantRemarks && (
                <Typography
                  color="textPrimary"
                  // variant="h8"
                  className={classes.tenantTextResponse}
                  variant="caption"
                >
                  {tenantRemarks}
                </Typography>
              )}
              {tenantRectificationImage !== "No image" && (
                <img
                  src={tenantRectificationImage}
                  className={classes.image}
                ></img>
              )}
            </Box>
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
          </AccordionDetails>
        </Accordion>
      )}{" "}
    </>
  );
}

export default AuditReportCard;
