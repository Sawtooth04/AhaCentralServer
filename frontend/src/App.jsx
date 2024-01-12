import {BrowserRouter, Route, Routes} from "react-router-dom";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";

function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<div />}/>
            <Route path="/registration" element={<div />}/>
            <Route path="*" element={<PrivateRoute />}/>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
