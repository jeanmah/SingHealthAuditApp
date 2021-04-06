//import logo from "./logo.svg";
import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import AuditorHome from "./pages/AuditorHome";
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
import TenantHome from "./pages/TenantHome";
import ManagerHome from "./pages/ManagerHome";
import Store from "./pages/Store";
import EditAccount from "./pages/EditAccount";
import EditPassword from "./pages/EditPassword";
import Chat from "./pages/Chat";

function App() {
  return (
    <Router>
      <ContextProvider>
        <Switch>
          <Route exact path="/" component={LoginComponent} />
          <AuthenticatedRoute exact path="/home/a" component={AuditorHome} />
          <AuthenticatedRoute exact path="/home/t" component={TenantHome} />
          <AuthenticatedRoute exact path="/home/m" component={ManagerHome} />
          <AuthenticatedRoute exact path="/account" component={Account} />
          <AuthenticatedRoute exact path="/edit_account" component={EditAccount} />
          <AuthenticatedRoute exact path="/edit_password" component={EditPassword} />
          <AuthenticatedRoute exact path="/institutions" component={Institutions} />
          <AuthenticatedRoute exact path="/institution/:institutionid" component={Institution} />
          <AuthenticatedRoute exact path="/tenant/:tenantId" component={Tenant} />
          <AuthenticatedRoute exact path="/tenant/fbChecklist/:tenantId" component={FbChecklist} />
          <AuthenticatedRoute exact path="/store" component={Store} />
          <AuthenticatedRoute exact path="/chat" component={Chat} />
          <AuthenticatedRoute exact path="/error" component={Error} />
        </Switch>
      </ContextProvider>
    </Router>
  );
}

export default App;
