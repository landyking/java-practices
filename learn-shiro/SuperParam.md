```
    public SuperParam getSuperParam() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            SuperParam superParam = (SuperParam) request.getAttribute(SUPER_PARAM_KEY);
            if (superParam != null) {
                return superParam;
            }
            String contentType = request.getContentType();
            if (StringUtils.hasText(contentType)) {
                if (contentType.toUpperCase().contains("JSON")) {
                    JSONObject root = JSONObject.parseObject(CharStreams.toString(new InputStreamReader(request.getInputStream(), Charsets.UTF_8)));
                    if (root == null) {
                        root = QuickJson.object();
                    }
                    JSONObject obj = QuickJson.convertParameterMapToJsonObject(request.getParameterMap());
                    superParam = new SuperParam(obj, root);
                }
            }
            if (superParam == null) {
                superParam = new SuperParam(QuickJson.convertParameterMapToJsonObject(request.getParameterMap()), QuickJson.object());
            }
            request.setAttribute(SUPER_PARAM_KEY, superParam);
            return superParam;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
```