import React from 'react';
import styles from './style.module.css'
import Sidebar from "../Sidebar/Sidebar";

const Content = () => {
    return (
        <div className={styles.content}>
            <Sidebar/>
        </div>
    );
};

export default Content;