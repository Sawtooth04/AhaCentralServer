import React from 'react';
import styles from './style.module.css'
import GroupFileRightsListItem from "../GroupFileRightsListItem/GroupFileRightsListItem";

const GroupFileRightsList = ({ groups, fileRights, onFileRightClick, isFileRightIncluded }) => {
    return (
        <div className={styles.groupFileRightsList}>
            { groups.map((item) => {
                return <GroupFileRightsListItem key={item.groupID} group={item} fileRights={fileRights} onFileRightClick={onFileRightClick}
                    isFileRightIncluded={isFileRightIncluded}/>
            }) }
        </div>
    );
};

export default GroupFileRightsList;