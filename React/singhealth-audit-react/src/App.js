import React, { Component } from 'react';
import './App.css';
import AuditorApp from './component/login_logout/AuditorApp.jsx';

class App extends Component {
  render() {
    return (
      <div className="container">
        <AuditorApp />
      </div>
    );
  }
}

export default App;
