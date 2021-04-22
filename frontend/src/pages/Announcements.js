import React, { useState, useContext, useEffect } from "react";
import { Link } from "react-router-dom";
import {
  Button,
  IconButton,
  TextField,
  FormControl,
  InputLabel,
  Select,
  Typography,
  Grid,
} from "@material-ui/core";
import {
  InputAdornment,
  DialogActions,
  DialogContent,
  DialogTitle,
  Dialog,
  DialogContentText,
} from "@material-ui/core";
import SearchIcon from "@material-ui/icons/Search";

import Navbar from "../Navbar";
import useStyles from "../styles";
import { Context } from "../Context";
import NotificationRow from "../components/NotificationRow";

const Announcement = () => {
  const styles = useStyles();
  const {
    accountState,
    allAvailableNotificationsState,
    getAllAvailableNotifications,
    currentNotificationsState,
    getCurrentNotifications,
    notificationsByNotificationIdState,
    getNotificationByNotificationId,
    chatSubmitState,
  } = useContext(Context);

  const [
    displayedNotificationsState,
    setDisplayedNotificationsState,
  ] = useState([]);
  const [notificationRangeState, setNotificationRangeState] = useState(
    "current"
  );
  const [searchBarInputState, setSearchBarInputState] = useState("");

  const { role_id } = accountState;

  function handleSearchBarChange(search_input) {
    setSearchBarInputState(search_input);
  }

  function handleClick() {
    //setDisplayedNotificationsState(allAvailableNotificationsState);
    console.log("Search Button Clicked!");
    console.log(searchBarInputState);
    console.log("REAL notificationsState: " + allAvailableNotificationsState);
  }

  useEffect(() => {
    async function getResponse(role_id) {
      try {
        await getAllAvailableNotifications().then((response) => {
          console.log(response);
          console.log("All available notifications: " + response.data);
          setDisplayedNotificationsState(response.data);
          console.log("Notifications state: " + displayedNotificationsState);
        });
      } catch {
        console.log("Failed to retrive allAvailableNotifications");
      }

      // try{
      //   await getCurrentNotifications().then((response) => {
      //     console.log("Current notifications: " + response.data);
      //     setNotificationsState(response.data);
      //     console.log("Notifications state: " + notificationsState);
      //   })
      // } catch {
      //   console.log("Failed to retrive currentNotifications");
      // }
    }
    getResponse();
    //getCurrentNotifications();
    //getNotificationByNotificationId();
  }, [chatSubmitState]);

  return (
    <main className={styles.main}>
      <Navbar />
      <div className={styles.body}>
        <TextField
          className={styles.search_bar}
          label="Search Notification ID/Creator ID"
          variant="outlined"
          InputProps={{
            endAdornment: (
              <InputAdornment>
                <IconButton onClick={handleClick}>
                  <SearchIcon />
                </IconButton>
              </InputAdornment>
            ),
          }}
          onChange={(e) => handleSearchBarChange(e.target.value)}
        />
        <div className={styles.annoucement_title_div}>
          <Typography variant="h6" className={styles.annoucement_title}>
            Announcements
          </Typography>
        </div>
        <div className={styles.announcement_list}>
          {displayedNotificationsState.map((notification, index) => {
            return (
              <React.Fragment key={index}>
                <div className={styles.announcement_bubble}>
                  <Grid item xs={12} sm container>
                    <Grid item xs container direction="column" spacing={2}>
                      <Grid item xs>
                        <Typography variant="subtitle2" color="textSecondary">
                          {notification.title}
                        </Typography>
                        <Typography variant="body1">
                          {notification.message}
                        </Typography>
                      </Grid>
                    </Grid>
                    <Grid item>
                      <Typography variant="body2" color="textSecondary">
                        Announcement ID: {notification.notification_id}
                      </Typography>
                      <Typography variant="body2" color="textSecondary">
                        Posted by {notification.creator_id} on{" "}
                        {notification.receipt_date}
                      </Typography>
                      <Typography variant="body2" color="textSecondary">
                        Valid period: {notification.receipt_date} to{" "}
                        {notification.end_date}
                      </Typography>
                    </Grid>
                  </Grid>
                </div>
              </React.Fragment>
            );
          })}
        </div>
      </div>
    </main>
  );
};

export default Announcement;
