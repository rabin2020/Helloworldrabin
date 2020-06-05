package com.emc.vplex.restful.handlers;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jcrontab.data.CrontabEntryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.emc.vplex.restful.util.JsonRenderer;
import com.emc.vplex.smsv2.business.BusinessException;
import com.emc.vplex.smsv2.business.ISystem;

public class MetaDataBackupHandlerTest extends SystemMocks {
  protected static String hourAndMin = "22 10 * * *";
  public static final String BACKUP_COMMAND = "metadatabackup local";

  private static class MockMetaDataBackupHandler extends MetaDataBackupHandler {

    protected List<BusinessException> errors = new ArrayList<>();
    protected JsonRenderer renderer = mock(JsonRenderer.class);

    public MockMetaDataBackupHandler(ISystem so) {
      super(so);
    }

    @Override
    public JsonRenderer getDefaultFieldsRenderer(JsonResponse response) {
      return renderer;
    }
  }

  private MockMetaDataBackupHandler handler;
  private JsonResponse response = Mockito.spy(new JsonResponse(mock(HttpServletResponse.class)));

  @Before
  public void setUp() throws BusinessException {
    setUpSystemMocks();
    handler = new MockMetaDataBackupHandler(so);
    response.setRenderer(handler.renderer);
  }

  @Test
  public void create() throws JsonHttpException, CrontabEntryException {
    MetaDataBackupHandler obj = Mockito.spy(handler);
    HttpServletRequest request = mock(HttpServletRequest.class);
    PathParameterProcessor path = new PathParameterProcessor(so, META_DATABACKUP_URI);

    Map<String, Object> json = new HashMap<>();
    json.put("storage_volumes", Arrays.asList(SVOL1_URI, SVOL2_URI));
    json.put("hours", "22");
    json.put("minutes", "10");

    // doNothing().when(handler).test(any(), any());
    Mockito.doReturn(true).when(obj).test();
    handler.post(path, json, request, response);
    verify(response).respondCreated(eq(META_DATABACKUP_URI), jmoEq(metaVol));
  }

}
