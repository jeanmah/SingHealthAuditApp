import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import Navbar from "../Navbar";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
}));

function AuditReport() {
  const { reportId } = useParams();

  const classes = useStyles();

  useEffect(() => {}, []);
  return (
    <div>
      <Navbar />
      <Box className={classes.header} textAlign="center" boxShadow={1}>
        <Typography variant="h5">Report</Typography>
      </Box>
    </div>
  );
}

export default AuditReport;
