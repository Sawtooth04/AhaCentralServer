import React from 'react';
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";
import {useNavigate} from "react-router-dom";

const AccessDeniedRedirectForm = ({ isHidden }) => {
    const navigate = useNavigate();

    return (
        <PopUpForm header={`Недостаточно прав для перехода над данную вкладку`} isHidden={isHidden}>
            <Button text={'На главную'} onClick={() => navigate('/main')}/>
        </PopUpForm>
    );
};

export default AccessDeniedRedirectForm;