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
import FbChecklist from "./pages/FbChecklist";
import AuthenticatedRoute from "./components/testJwt/AuthenticatedRoute";
import LoginComponent from "./components/testJwt/Login";
// import FbCategoryB from "./pages/fbChecklistPages/FbCategoryB";
// import FbCategoryC from "./pages/fbChecklistPages/FbCategoryC";
// import FbCategoryD from "./pages/fbChecklistPages/FbCategoryD";
// import FbCategoryE from "./pages/fbChecklistPages/FbCategoryE";
import { ContextProvider } from "./Context";

function App() {
  return (
    <Router>
      <ContextProvider>
        <Navbar />
        <Switch>
          <Route exact path="/login">
            <LoginComponent />
          </Route>
          <AuthenticatedRoute exact path="/home">
            <Home />
          </AuthenticatedRoute>
          <Route exact path="/account">
            <Account />
          </Route>
          <Route exact path="/institutions">
            <Institutions />
          </Route>
          <Route exact path="/institution/:institutionid">
            <Institution />
          </Route>
          <Route exact path="/tenant/:tenantId">
            <Tenant />
          </Route>
          <Route exact path="/tenant/fbChecklist/:tenantId">
            <FbChecklist />
          </Route>
          <Route path="*">
            <Error />
          </Route>
        </Switch>
      </ContextProvider>
    </Router>
  );
}

export default App;
