import logo from "./logo.svg";
import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Navbar from "./Navbar";
import Home from "./pages/Home";
import Account from "./pages/Account";
import Tenant from "./pages/Tenant";
import Error from "./pages/Error";
import Institutions from "./pages/Institutions";
import Institution from "./pages/Institution";
import FbCategoryA from "./pages/fbChecklistPages/FbCategoryA";
import FbCategoryB from "./pages/fbChecklistPages/FbCategoryB";
import FbCategoryC from "./pages/fbChecklistPages/FbCategoryC";
import FbCategoryD from "./pages/fbChecklistPages/FbCategoryD";
import FbCategoryE from "./pages/fbChecklistPages/FbCategoryE";

function App() {
  return (
    <Router>
      <Navbar />
      <Switch>
        <Route exact path="/">
          <Home />
        </Route>
        <Route exact path="/account">
          <Account />
        </Route>
        <Route exact path="/institutions">
          <Institutions />
        </Route>
        <Route exact path="/institution/:id">
          <Institution />
        </Route>
        <Route exact path="/tenant/:id">
          <Tenant />
        </Route>
        <Route exact path="/fbCategoryA">
          <FbCategoryA />
        </Route>
        <Route exact path="/fbCategoryB">
          <FbCategoryB />
        </Route>
        <Route exact path="/fbCategoryC">
          <FbCategoryC />
        </Route>
        <Route exact path="/fbCategoryD">
          <FbCategoryD />
        </Route>
        <Route exact path="/fbCategoryE">
          <FbCategoryE />
        </Route>
        <Route path="*">
          <Error />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
