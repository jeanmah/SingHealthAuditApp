import React, { Component } from 'react';
import HomeService from '../../service/home_pages/HomeService';

import { Link } from 'react-router-dom';

class ManagerHomeComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            idkwhatyet: null,
            message: null
        }
        this.refreshManagerHome = this.refreshManagerHome.bind(this);
    }

    componentDidMount() {
        this.refreshManagerHome();
    }

    refreshManagerHome() {
        HomeService.loadHomePage('m')
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ idkwhatyet: response.data })
                }
            )
            .catch(err => console.log("error " + err));

    }
    
    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>Manager Home Page</h3>
                <div className="container">
            
                    <p>Welcome {this.state.idkwhatyet}</p>
                </div>
            </div>
        )
    }
}

export default ManagerHomeComponent