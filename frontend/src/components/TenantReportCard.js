import React, { useState, useContext, useEffect } from "react";
import TextField from "@material-ui/core/TextField";
import { makeStyles } from "@material-ui/core/styles";
import { Context } from "../Context";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";

import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import Box from "@material-ui/core/Box";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import PhotoCamera from "@material-ui/icons/PhotoCamera";
import { Typography } from "@material-ui/core";
import { useForm } from "react-hook-form";

const useStyles = makeStyles((theme) => ({
  titleResolved: {
    padding: theme.spacing(0, 2, 0, 2),
    color: "#F15A22",
  },
  commentBox: {
    maxWidth: 800,
    width: "100%",
    padding: theme.spacing(0, 0, 2, 2),
  },
  comment: {
    width: "100%",
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
  tenantInstructions: {
    display: "flex",
    padding: theme.spacing(2, 0, 2, 2),
  },
  tenantResponse: {
    display: "flex",
    width: "100%",
  },
  avatar: {},
  button: {
    width: "50%",
    color: "#F15A22",
    fontWeight: "medium",
    backgroundColor: theme.palette.background.default,
  },
  buttonContainer: {
    padding: theme.spacing(4, 0, 4, 0),
    display: "flex",
    justifyContent: "center",
  },
  input: {
    display: "none",
  },
  camera: {
    display: "flex",
    flexDirection: "column",
    //   alignItems: "center",
  },
  uploadText: {
    padding: theme.spacing(0, 1, 0, 2),
  },
}));

function TenantReportCard({
  original_remarks,
  qn_id,
  requirement,
  timeframe,
  report_id,
  tenant_id,
  current_qn_status,
  severity,
}) {
  const classes = useStyles();
  const [comment, setComment] = useState("");
  //selected file state
  // const [selectedFile, setSelectedFile] = useState();
  const [imageState, setImageState] = useState([]);
  const [tenantResponse, setTenantResponse] = useState();

  //state to check if file is selected
  // const [isFilePicked, setIsFilePicked] = useState(false);

  const {
    getReportEntry,
    submitReportUpdate,
    getTenantRectification,
  } = useContext(Context);

  //call when component mounts
  useEffect(() => {
    async function getResponse() {
      try {
        //GET REPORT TYPE

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

  //function to update comment state
  const handleComment = (e) => {
    setComment(e.target.value);
  };

  //function to handle input file change
  const handleChange = (e) => {
    const getBase64 = (file) => {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();

        if (file) {
          reader.readAsDataURL(file);
        }

        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    };

    getBase64(e.target.files[0]).then((image) => {
      setImageState(image);
    });
  };

  // let input = document.getElementsByTagName("input")[0];

  // input.onclick = function () {
  //   this.value = null;
  // };

  // input.onchange = (event) => {
  //   setSelectedFile(event.target.files[0]);
  //   console.log(event.target.files[0]);
  //   setIsFilePicked(true);
  // };
  const createEntry = (
    questionId,
    currentStatus,
    image,
    remark,
    severityLevel
  ) => {
    let array = [];
    array.push({
      qn_id: questionId,
      status: currentStatus,
      severity: severityLevel,
      remarks: remark,
      images: image,
    });
    return array;
  };

  //handle submit
  const handleSubmit = () => {
    // console.log(testState);
    console.log(imageState);

    async function submitAsync() {
      const entry = await createEntry(
        qn_id,
        current_qn_status,
        imageState,
        comment,
        severity
      ).then((response) => {
        return response;
      });

      submitReportUpdate(report_id, false, "", entry);

      alert("Rectification submitted. Pending Approval");
    }
    submitAsync();

    // async function getEntry() {
    //   const entry = await getReportEntry(report_id, entry_id).then(
    //     (response) => {
    //       console.log(response.data);
    //       return response.data;
    //     }
    //   );

    // submitReportUpdate(report_id, false, "", {
    //   ...entry,
    //   images: [imageState],
    //   remarks: comment,
    // });

    // alert("Rectification submitted. Pending Approval");
    // }
    // getEntry();
  };

  return (
    <Accordion>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
        aria-label="Expand"
        aria-controls="additional-actions1-content"
        id="additional-actions1-header"
        // id={`additional-actions1-header${entry_id}`}
      >
        <ListItem>
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
            REMARKS: {original_remarks}
          </Typography>

          <Typography
            color="textSecondary"
            variant="button"
            className={classes.textInfo}
          >
            RECTIFICATION PERIOD: {timeframe}
          </Typography>
        </div>

        <div className={classes.tenantInstructions}>
          <Avatar src="/broken-image.jpg" className={classes.avatar} />
          <Typography color="textPrimary" className={classes.textTenant}>
            Please respond to non-compliance below:
          </Typography>
        </div>
        <div className={classes.tenantResponse}>
          <TextField
            id="standard-multiline-static"
            label="Comment on rectification to non-compliance"
            multiline
            value={comment}
            className={classes.comment}
            onChange={(e) => {
              handleComment(e);
            }}
          />
          <input
            // accept="image/*"
            className={classes.input}
            id={`icon-button-file${qn_id}`}
            // id="icon-button-file"
            name={`file${qn_id}`}
            type="file"
            // value={null}
            // name="picture"
            onClick={(e) => {
              e.target.value = "";
            }}
            onChange={(e) => {
              handleChange(e);
            }}
          />
          <label
            htmlFor={`icon-button-file${qn_id}`}
            // htmlFor="icon-button-file"
            className={classes.camera}
          >
            <IconButton
              color="primary"
              aria-label="upload picture"
              component="span"
            >
              <Typography variant="button" className={classes.uploadText}>
                Upload photo
              </Typography>
              <PhotoCamera />
            </IconButton>
          </label>
        </div>
        {tenantResponse.map((response) => {
          const { remarks, images } = response;

          return (
            <>
              <Box className={classes.tenantResponseContainer} boxShadow={2}>
                <div className={classes.tenantResponse}>
                  <Avatar src="/broken-image.jpg" className={classes.avatar} />
                  <Typography
                    color="textPrimary"
                    className={classes.textTenant}
                  >
                    Your Previous Response(s):
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
            </>
          );
        })}
        <div className={classes.buttonContainer}>
          <Button
            className={classes.button}
            size="large"
            onClick={() => {
              handleSubmit();
            }}
            // color="secondary"
          >
            submit
          </Button>
        </div>
      </AccordionDetails>
    </Accordion>
  );
}

export default TenantReportCard;
