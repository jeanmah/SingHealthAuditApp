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
        <Switch>
          <Route exact path="/" exact component={LoginComponent} />
          <AuthenticatedRoute exact path="/home/a" exact component={Home} />
          <AuthenticatedRoute exact path="/account" exact component={Account} />
          <AuthenticatedRoute
            exact
            path="/institutions"
            exact
            component={Institutions}
          />
          <AuthenticatedRoute
            exact
            path="/institution/:institutionName"
            exact
            component={Institution}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/:tenantId"
            exact
            component={Tenant}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/fbChecklist/:tenantId"
            exact
            component={FbChecklist}
          />
          <AuthenticatedRoute exact path="/error" exact component={Error} />
        </Switch>
      </ContextProvider>
    </Router>
  );
}

export default App;
