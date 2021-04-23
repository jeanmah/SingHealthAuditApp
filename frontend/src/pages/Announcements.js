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
import LimitedNotificationRow from "../components/LimitedNotificationRow";

const Announcement = () => {
  const styles = useStyles();
  const {
    accountState,
    allAvailableNotificationsState,
    getAllAvailableNotifications,
    currentNotificationsState,
    getNotificationByNotificationId,
    chatSubmitState,
  } = useContext(Context);

<<<<<<< HEAD
  const [
    displayedNotificationsState,
    setDisplayedNotificationsState,
  ] = useState([]);
  const [notificationRangeState, setNotificationRangeState] = useState(
    "current"
  );
=======
  const [displayedNotificationsState, setDisplayedNotificationsState] = useState([]);
  const [rangeState, setRangeState] = useState("all");
>>>>>>> f83e239ca5bec302184503e8dd2087fb24ec9544
  const [searchBarInputState, setSearchBarInputState] = useState("");

  const { role_id } = accountState;

  function handleSearchBarChange(search_input) {
    setSearchBarInputState(parseInt(search_input)); // String => Integer
  }

  function handleSearchButtonClick() {
    console.log("Submitting search bar input: " + searchBarInputState);
    console.log(typeof searchBarInputState);
    if (searchBarInputState < 1000 && searchBarInputState > 0) {
      setRangeState("by_notification_id");
      console.log("Setting range to By Institution ID");
    } else if (searchBarInputState >= 1000) {
      setRangeState("by_manager_id");
      console.log("Setting range to By Manager ID");
    } else {
      setRangeState("all");
      console.log("Setting range to All");
    }
    console.log("Searchbar input: " + searchBarInputState);
    console.log("Current range: " + rangeState);
  }

  useEffect(() => {
<<<<<<< HEAD
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
=======

    async function getResponse() {
        try{
          await getAllAvailableNotifications().then((response) => {
            console.log("All available notifications: " + response.data);
            setDisplayedNotificationsState(response.data);
          })
        } catch {
          console.log("Failed to retrive allAvailableNotifications");
        }
    };
>>>>>>> f83e239ca5bec302184503e8dd2087fb24ec9544
    getResponse();

  }, [chatSubmitState]);

  return (
    <main className={styles.main}>
      <Navbar />
      <div className={styles.body}>
        <TextField
<<<<<<< HEAD
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
=======
          className={styles.search_bar} 
          label="Search Notification ID/Creator ID" 
          variant="outlined" 
          InputProps={{endAdornment: (<InputAdornment><IconButton onClick={handleSearchButtonClick}><SearchIcon/></IconButton></InputAdornment>)}}
>>>>>>> f83e239ca5bec302184503e8dd2087fb24ec9544
          onChange={(e) => handleSearchBarChange(e.target.value)}
        />
        <div className={styles.annoucement_title_div}>
          <Typography variant="h6" className={styles.annoucement_title}>
            Announcements
          </Typography>
        </div>
        <div className={styles.announcement_list}>
          {displayedNotificationsState.map((notification, index) => {
<<<<<<< HEAD
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
=======
            if (rangeState === "all") {
              return <LimitedNotificationRow notification={notification} key={index}/>
            } else if (rangeState === "by_notification_id" && notification.notification_id === searchBarInputState) {
              return <LimitedNotificationRow notification={notification} key={index}/>
            } else if (rangeState === "by_manager_id" && notification.creator_id === searchBarInputState) {
              return <LimitedNotificationRow notification={notification} key={index}/>
            } else {
              return null;
            }
>>>>>>> f83e239ca5bec302184503e8dd2087fb24ec9544
          })}
        </div>
      </div>
    </main>
  );
};

export default Announcement;
