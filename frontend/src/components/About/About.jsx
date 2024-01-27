import React from 'react';
import styles from "./style.module.css";

const About = () => {
    return (
        <div className={styles.about}>
            <h1 className={styles.aboutHeading}> О системе </h1>
            <p className={styles.aboutText}>
                Aha Storage System - распределённая система хранения файлов, предназначенная для некоммерческого частного
                использования.
            </p>
        </div>
    );
};

export default About;