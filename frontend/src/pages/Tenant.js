import React, { useContext, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Context } from "../Context";
import Navbar from "../Navbar";
import Loading from "./Loading";
import { makeStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Collapse from "@material-ui/core/Collapse";
import ExpandLess from "@material-ui/icons/ExpandLess";
import ExpandMore from "@material-ui/icons/ExpandMore";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import QuestionAnswerIcon from "@material-ui/icons/QuestionAnswer";
import HistoryIcon from "@material-ui/icons/History";
import AssignmentTurnedInIcon from "@material-ui/icons/AssignmentTurnedIn";
import FastfoodIcon from "@material-ui/icons/Fastfood";
import StoreIcon from "@material-ui/icons/Store";
import LocalHospitalIcon from "@material-ui/icons/LocalHospital";
import Grid from "@material-ui/core/Grid";

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
    // margin: theme.spacing(4, 0, 10, 0),
  },
  nested: {
    paddingLeft: theme.spacing(4),
  },
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
  listItem: {
    padding: theme.spacing(2, 2, 2, 2),
  },
}));

function Tenant() {
  //get tenantid from url
  const { tenantId } = useParams();
  //tenant state
  const [tenantState, setTenantState] = useState();
  const [open, setOpen] = useState(false);
  //Context: getUserInfo method
  const { getUserInfo } = useContext(Context);

  const classes = useStyles();

  useEffect(() => {
    getUserInfo(tenantId)
      .then((response) => {
        console.log(response);
        setTenantState(response.data);
      })
      .catch(() => {
        console.log("Failed to retrieve tenant info");
      });
  }, []);

  const handleClick = () => {
    setOpen(!open);
  };

  return (
    <div>
      {tenantState ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">{tenantState.store_name}</Typography>
          </Box>
          <Grid container className={classes.root}>
            <List
              component="nav"
              aria-labelledby="nested-list-subheader"
              className={classes.list}
            >
              <ListItem button divider={true} className={classes.listItem}>
                <ListItemIcon>
                  <QuestionAnswerIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="View Chats" />
              </ListItem>
              <ListItem button divider={true} className={classes.listItem}>
                <ListItemIcon>
                  <HistoryIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="View Previous Audits" />
              </ListItem>
              <ListItem
                button
                onClick={handleClick}
                divider={true}
                className={classes.listItem}
              >
                <ListItemIcon>
                  <AssignmentTurnedInIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="Select Checklist" />
                {open ? <ExpandLess /> : <ExpandMore />}
              </ListItem>
              <Collapse in={open} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  <Link to={`/tenant/fbChecklist/${tenantId}`}>
                    <ListItem button className={classes.nested}>
                      <ListItemIcon>
                        <FastfoodIcon color="secondary" />
                      </ListItemIcon>
                      <ListItemText primary="Conduct F&B Audit" />
                    </ListItem>
                  </Link>
                  <ListItem button className={classes.nested}>
                    <ListItemIcon>
                      <StoreIcon color="secondary" />
                    </ListItemIcon>
                    <ListItemText primary="Conduct Non-F&B Audit" />
                  </ListItem>
                  <ListItem button className={classes.nested}>
                    <ListItemIcon>
                      <LocalHospitalIcon color="secondary" />
                    </ListItemIcon>
                    <ListItemText primary="Conduct Safe Managment Audit" />
                  </ListItem>
                </List>
              </Collapse>
            </List>
          </Grid>
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default Tenant;
