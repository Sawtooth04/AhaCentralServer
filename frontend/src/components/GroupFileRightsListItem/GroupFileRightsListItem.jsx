import React, {useState} from 'react';
import styles from './style.module.css'
import GetFileRightName from "../../utils/FileRightNameProvider";

const GroupFileRightsListItem = ({ group, fileRights, onFileRightClick, isFileRightIncluded }) => {
    const [isFileRightsHidden, setIsFileRightsHidden] = useState(true);

    function onClick() {
        setIsFileRightsHidden(!isFileRightsHidden);
    }

    function onFileRightClickHandler(event, fileRight) {
        event.stopPropagation();
        onFileRightClick(group, fileRight);
    }

    return (
        <div className={styles.groupFileRightsListItem} onClick={onClick}>
            <p className={styles.groupName}> { group.name } </p>
            <div className={isFileRightsHidden ? styles.fileRightsWrapperHidden : styles.fileRightsWrapper}>
                { fileRights.map((item) => {
                    return <p className={isFileRightIncluded(group.groupID, item.fileRightID) ? styles.fileRightSelected : styles.fileRight}
                        onClick={(event) => {onFileRightClickHandler(event, item)}}
                        key={item.fileRightID}>
                        {GetFileRightName(item.name)}
                    </p>
                }) }
            </div>
        </div>
    );
};

export default GroupFileRightsListItem;