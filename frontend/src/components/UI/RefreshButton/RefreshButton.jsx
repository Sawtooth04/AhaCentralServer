import React, {useEffect, useState} from 'react';
import styles from './style.module.css'

const RefreshButton = ({ onClick }) => {
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        async function processing() {
            await onClick();
            setIsProcessing(false);
        }

        if (isProcessing)
            void processing();
    }, [isProcessing, onClick]);
    
    function onButtonClick() {
        setIsProcessing(true);
    }

    return (
        <button className={styles.refreshButton} onClick={onButtonClick}>
            <img className={isProcessing ? styles.refreshButtonRotatingImage : styles.refreshButtonImage}
                src={"assets/icons/refresh.png"} alt={"Icon"}/>
        </button>
    );
};

export default RefreshButton;