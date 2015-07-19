<%--
  ~ Copyright 2015 Karl Bennett
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<!DOCTYPE html>
<html>
<head>
    <title>Sign In</title>
</head>
<body>
<h1>Sign In</h1>

<form method="POST">
    <label for="username">User Name:</label>
    <input type="text" name="username" id="username"/>

    <label for="password">Password:</label>
    <input type="password" name="password" id="password"/>

    <input type="submit" value="Sign In"/>
</form>
</body>
</html>