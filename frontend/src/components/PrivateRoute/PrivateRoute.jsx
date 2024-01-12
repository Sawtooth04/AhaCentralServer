import React from 'react';
import styles from './style.module.css'
import Header from "../Header/Header";
import Content from "../Content/Content";

const PrivateRoute = () => {
    return (
        <div className={styles.privateRoute}>
            <Header/>
            <Content/>
        </div>
    );
};

export default PrivateRoute;