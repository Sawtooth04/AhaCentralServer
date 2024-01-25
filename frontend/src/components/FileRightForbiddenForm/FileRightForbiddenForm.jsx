import React from 'react';
import Button from "../UI/Button/Button";
import PopUpForm from "../UI/PopUpForm/PopUpForm";

const FileRightForbiddenForm = ({ isHidden, setIsHidden }) => {
    return (
        <PopUpForm header={`Недостаточно прав для выполнения данной операции`} isHidden={isHidden}>
            <Button text={'Закрыть'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default FileRightForbiddenForm;